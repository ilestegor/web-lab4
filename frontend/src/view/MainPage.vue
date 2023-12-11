<script>


import PaginationTable from "@/component/PaginationTable.vue";
import useVuelidate from "@vuelidate/core";
import {between, required} from "@vuelidate/validators";
import MyButton from "@/component/Button.vue";

const RED_COLOR = "#00fff7";
const GREEN_COLOR = "#aa00ff"
export default {
  name: "MainPage",
  components: {MyButton, PaginationTable},
  data() {
    return {
      headersArray: [
        {label: "x", value: "X"},
        {label: "y", value: "Y"},
        {label: "r", value: "R"},
        {label: "curRequestTime", value: "Current Time"},
        {label: "executionTime", value: "Execution Time"},
        {label: "hitType", value: "Hit Type"},
      ],
      calculator: null,
      calcElement: null,
      xValue: "",
      yValue: "",
      rValue: "",
      coordinatesFromGraph: null,
      v1$: useVuelidate(),
      v2$: useVuelidate(),
      v3$: useVuelidate(),
      COLORS: {
        RED_COLOR,
        GREEN_COLOR
      },
      resultArray: [],
      graphInitialState: null,
      windowWidth: window.innerWidth
    }
  },
  validations() {
    return {
      xValue: {required, between: between(-5, 5)},
      yValue: {required, between: between(-5, 3)},
      rValue: {required, between: between(-5, 5)}
    }
  },
  async mounted() {
    let elt = document.getElementById('calculator');
    let calculator = Desmos.GraphingCalculator(elt, {
      keypad: false,
      expressions: false,
      settingsMenu: false,
      invertedColors: true,
      xAxisLabel: 'x',
      yAxisLabel: 'y',
      xAxisStep: 1,
      yAxisStep: 1,
      lockViewport: true,
      xAxisArrowMode: Desmos.AxisArrowModes.POSITIVE,
      yAxisArrowMode: Desmos.AxisArrowModes.POSITIVE,
    });
    calculator.setMathBounds({
      left: -5,
      right: 5,
      bottom: -5,
      top: 5
    });
    this.calculator = calculator;
    this.calcElement = elt;
    this.graphInitialState = this.calculator.getState();
    await this.getAllResults();
    await this.$nextTick(() => {
      window.addEventListener('resize', this.onResize);
    })
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.onResize);
  },
  methods: {
    onResize() {
      this.windowWidth = window.innerWidth
    },
    drawGraphByR(r) {
      r = +r
      if (isNaN(Number(r)) || !this.validateR(r)) r = 0
      if (!isNaN(Number(r))) {
        this.calculator.setExpression({
          id: 'area-1',
          latex: 'y\\le ' + r + '\\left\\{0\\le-x\\le ' + r + '\\right\\}\\left\\{y\\ge0\\right\\}'
        });
        this.calculator.setExpression({
          id: 'area-2',
          latex: 'y\\ge-x-\\frac{' + r + '}{2}\\left\\{x\\le0\\right\\}\\left\\{y\\le0\\right\\}'
        });
        this.calculator.setExpression({
          id: 'area-3',
          latex: 'x^{2}+y^{2}\\le ' + r + '^{2}\\left\\{x\\ge0\\right\\}\\left\\{y\\le0\\right\\}'
        })
      }
    },
    drawDots(x, y, color) {
      if (x !== null && y !== null) {
        this.calculator.setExpression({
          id: x + '' + y,
          color: color,
          latex: '\\left(' + x + ',' + y + '\\right)',
        })
      }
    },
    checkArea(x, y, r) {
      if (this.validateX(x) && this.validateY(y) && this.validateR(r)) {
        if (y <= r && (-x >= 0) && (r >= -x) && (y >= 0))
          return true;
        if (y >= -x - (r / 2) && (x <= 0) && (y <= 0))
          return true
        return (x * x + y * y <= r * r) && (x >= 0) && (y <= 0);
      } else {
        return null
      }
    },
    drawDotByAreaHit(x, y, r) {
      const areaHit = this.checkArea(x, y, r);
      if (areaHit === null) {
        return;
      }
      if (areaHit) {
        this.drawDots(x, y, this.COLORS.GREEN_COLOR)
      } else this.drawDots(x, y, this.COLORS.RED_COLOR)

    },
    addResultToArray(x, y, r, curRequestTime, execTime, hit) {
      if (this.validateX(x) && this.validateY(y) && this.validateR(r)) {
        const result = {x: x, y: y, r: r, executionTime: execTime, curRequestTime: curRequestTime, hitType: hit}
        this.resultArray.push(result)
      }
    },
    calculateCordsByGraphClick(e) {
      const calcBounderies = this.calcElement.getBoundingClientRect();
      const x = e.clientX - calcBounderies.left;
      const y = e.clientY - calcBounderies.top;
      this.coordinatesFromGraph = this.calculator.pixelsToMath({x: x, y: y});
    },
    changeDotsColorByR() {
      if (this.validateR(this.rValue)) {
        this.resultArray.forEach(dots => this.drawDotByAreaHit(dots.x, dots.y, this.rValue))
      } else {
        this.resultArray.forEach(dots => this.drawDots(dots.x, dots.y, this.COLORS.RED_COLOR))
      }
    },
    validateX(x) {
      return x >= -5 && x <= 5 && x !== ""
    },
    validateY(y) {
      return y >= -5 && y <= 3 && y !== "";
    },
    validateR(r) {
      return r >= -5 && r <= 5 && r !== "";
    },
    async addDotsRequest(x, y, r) {
      const data = {x: x, y: y, r: r}
      const url = "http://localhost:8080/lab4Spring/api/dots/add"
      const response = await this.$store.dispatch('auth/userMainPageRequest', {data, url})
      if (response === null) {
        this.$notify({group: 'main-errors', text: 'Server is down', type: 'warn'})
        return;
      }
      switch (response.status) {
        case 401:
          this.$notify({group: 'main-errors', text: 'Not authorized. Dots can not be loaded'});
          setTimeout(() => {
            this.$router.push("/auth");
          }, 3000)
          return false;
        case 400:
          const parsedResponse = await response.json();
          this.$notify({group: 'main-errors', text: parsedResponse.detailMessage, type: 'warn'});
          return false;
        case 200:
          return response.json();
      }
    },
    async addDotsByGraphClick(x, y, r) {
      if (!this.validateX(x)) {
        this.$notify({group: 'main-errors', text: 'X value is out of range', type: 'warn'})
      } else if (!this.validateY(y)) {
        this.$notify({group: 'main-errors', text: 'Y value is out of range', type: 'warn'})
      } else if (!this.validateR(r)) {
        this.$notify({group: 'main-errors', text: 'R value must be specified', type: 'warn'})
      } else {
        const response = await this.addDotsRequest(x, y, r);
        if (response)
          await this.addResultToArray(response.x, response.y, response.r, response.curRequestTime, response.executionTime, response.hitType)
      }
    },
    async addDotsByForm(x, y, r) {
      if (this.validateX(x) && this.validateY(y) && this.validateR(r)) {
        const response = await this.addDotsRequest(x, y, r);
        if (response)
          this.addResultToArray(response.x, response.y, response.r, response.curRequestTime, response.executionTime, response.hitType)
      } else {
        this.$notify({group: 'main-errors', text: 'Values in form are not valid'})
      }
    },
    async getAllResults() {
      const url = "http://localhost:8080/lab4Spring/api/dots/getDots"
      const method = 'get'
      const userResult = await this.$store.dispatch('auth/requestWithoutParams', {url, method})
      if (userResult !== null) {
        switch (userResult.status) {
          case 401:
            this.notAuthResponseHandler('Not authorized. Dots can not be loaded');
            return false;
          case 200:
            this.resultArray = await userResult.json()
            this.resultArray.forEach(result => this.drawDotByAreaHit(result.x, result.y, result.r));
            return true;
        }
      } else {
        this.$notify({group: 'main-errors', text: 'Server is down', type: 'warn'})
      }
    },
    async deleteDotsRequest() {
      const url = "http://localhost:8080/lab4Spring/api/dots/deleteDots";
      const method = 'delete'
      const response = await this.$store.dispatch('auth/requestWithoutParams', {url, method})
      if (response !== null) {
        switch (response.status) {
          case 200:
            const parsedResponse = await response.text();
            this.$notify({group: 'info', text: parsedResponse})
            this.clearLocalDots();
            this.calculator.setState(this.graphInitialState);
            this.$refs["my-form"].reset();
            break;
          case 400:
            const jsonResponse = await response.json();
            this.$notify({group: 'main-errors', text: jsonResponse.detailMessage, type: 'warn'})
        }
      } else {
        this.$notify({group: 'main-errors', text: 'Server is down', type: 'warn'})
      }
    },
    notAuthResponseHandler(message) {
      this.$notify({group: 'main-errors', text: message});
      setTimeout(() => {
        this.$router.push("/auth");
      }, 1000)
    },
    clearLocalDots() {
      this.resultArray = []
    },
    async logoutRequest() {
      const url = "http://localhost:8080/lab4Spring/api/auth/logout"
      const data = "";
      const logoutResponse = await this.$store.dispatch('auth/userMainPageRequest', {data, url})
      if (logoutResponse !== null) {
        switch (logoutResponse.status) {
          case 200:
            const parsedLogoutResponse = await logoutResponse.json();
            this.notAuthResponseHandler(parsedLogoutResponse.message);
            localStorage.removeItem("exp_date")
            break;
        }
      }
    },
    passMaxPerPageShown() {
      if (this.windowWidth < 550 || this.windowWidth > 874) {
        console.log(this.windowWidth)
        return 4;
      } else if (this.windowWidth < 874) {
        return 7;
      }
    }
  }
}
</script>

<template>
  <my-button id="close-btn" @click="logoutRequest"></my-button>
  <section class="main-section" id="media-section">
    <notifications group="main-errors"/>
    <notifications group="info"/>
    <content-box id="table-box">
      <p class="fs-16">History Table</p>
      <div class="pag-table">
        <pagination-table
            :array="this.resultArray"
            :content-per-page="this.passMaxPerPageShown()"
            :max-page-show="2"
            :headers="this.headersArray"
        />
      </div>
    </content-box>
    <content-box id="calculator-content-box">
      <div id="calculator"
           @click="calculateCordsByGraphClick($event); drawDotByAreaHit(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue);  addDotsByGraphClick(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue)">
      </div>
    </content-box>
    <content-box>
      <p class="fs-16">Input Form</p>
      <form @submit.prevent class="main-form" ref="my-form">
        <div class="main-form-input-wrapper">
          <custom-input label="X value" class="main-input" placeholder-text="Enter value from -5...5"
                        v-model:input-value="xValue" @input="this.v1$.$touch()">
          </custom-input>
          <p class="error-message" v-for="error2 in v1$.xValue.$errors" :key="error2.$uid">{{ error2.$message }}</p>
        </div>
        <div class="main-form-input-wrapper">
          <custom-input label="Y value" placeholder-text="Enter value from -5..3" v-model:input-value.trim="yValue"
                        @input="this.v2$.$touch()"></custom-input>
          <p class="error-message" v-for="error2 in v2$.yValue.$errors" :key="error2.$uid">{{ error2.$message }}</p>
        </div>
        <div class="main-form-input-wrapper">
          <custom-input label="R value" placeholder-text="Enter value from -5...5"
                        @input="drawGraphByR(this.rValue); changeDotsColorByR(); this.v3$.$touch()"
                        v-model:input-value.trim="rValue"></custom-input>
          <p class="error-message" v-for="error3 in v3$.rValue.$errors" :key="error3.$uid">{{ error3.$message }}</p>
        </div>
        <div class="form-buttons">
          <my-button
              @click="drawDotByAreaHit(this.xValue, this.yValue, this.rValue);  addDotsByForm(this.xValue, this.yValue, this.rValue)">
            Send
          </my-button>
          <my-button @click="deleteDotsRequest()">Clear</my-button>
        </div>
      </form>
    </content-box>
  </section>
</template>

<style>
.main-section {
  display: flex;
  justify-content: center;
  gap: 10px;
  width: 100%;
  margin-top: 20px;
}

.pag-table {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.fs-16 {
  font-size: 16px;
}

#calculator {
  width: 100%;
  max-width: 600px;
  min-width: 200px;
  height: 100%;
}

.form-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  gap: 5px;
}

.form-buttons > button {
  width: 46%;
}

.main-form > div {
  margin-top: 25px;
}

.input-wrap > label {
  margin-right: 10px;
}

.input-wrap > input {
  width: 70%;
}

.error-message {
  margin-top: 5px;
  font-size: 11px;
  color: #e54545;
}

#close-btn {
  box-sizing: border-box;
  display: inline-block;
  float: right;
  width: 33px;
  height: 33px;
  background-color: transparent;
  box-shadow: inset 0 0 0 2px #232931;
  border-radius: 50%;
  position: absolute;
  cursor: pointer;
  border: none;
  top: 30px;
  right: 30px;
  z-index: 10;
}

#close-btn:after,
#close-btn:before {
  content: "";
  position: absolute;
  background-color: #232931;
}

#close-btn:after {
  width: 2px;
  height: 30px;
  top: 1px;
  left: 15px;
  transform: rotate(45deg);
}

#close-btn:before {
  width: 30px;
  height: 2px;
  top: 15px;
  left: 1px;
  transform: rotate(45deg);
}

#close-btn:hover:after {
  transform: rotate(-90deg);
  transition: transform 0.5s;
}

#close-btn:hover:before {
  transform: rotate(-180deg);
  transition: transform 0.5s;
}

#table-box {
  min-width: 328px;
}

@media screen and (max-width: 874px) {
  #media-section {
    flex-direction: column;
  }

  #calculator {
    height: 350px;
  }

  #calculator-content-box {
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>