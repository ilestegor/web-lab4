<script>


import PaginationTable from "@/component/PaginationTable.vue";
import useVuelidate from "@vuelidate/core";
import {between, required} from "@vuelidate/validators";

const RED_COLOR = "#00fff7";
const GREEN_COLOR = "#aa00ff"
export default {
  name: "MainPage",
  components: {PaginationTable},
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
      graphInitialState: null
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
  },
  methods: {
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
          this.$router.push("/auth");
          break;
        case 400:
          const parsedResponse = await response.json();
          this.$notify({group: 'main-errors', text: parsedResponse.detailMessage, type: 'warn'});
          break;
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
        this.addResultToArray(response.x, response.y, response.r, response.executionTime, response.curRequestTime, response.hitType)
      }
    },
    async addDotsByForm(x, y, r) {
      if (this.validateX(x) && this.validateY(y) && this.validateR(r)) {
        const response = await this.addDotsRequest(x, y, r);
        this.addResultToArray(response.x, response.y, response.r, response.executionTime, response.curRequestTime, response.hitType)
      } else {
        this.$notify({group: 'main-errors', text: 'Values in form are not valid'})
      }
    },
    async getAllResults() {
      const userResult = await this.$store.dispatch('auth/userGetRequest', "http://localhost:8080/lab4Spring/api/dots/getDots")
      this.resultArray = await userResult.json()
      this.resultArray.forEach(result => this.drawDotByAreaHit(result.x, result.y, result.r))
    },
    async deleteDotsRequest() {
      const response = await this.$store.dispatch('auth/deleteRequest', "http://localhost:8080/lab4Spring/api/dots/deleteDots")
      if (response !== null) {
        const parsedResponse = await response.text();
        this.$notify({group: 'info', text: parsedResponse})
        this.clearLocalDots();
        this.calculator.setState(this.graphInitialState)
      }
    },
    clearLocalDots() {
      this.resultArray = []
    }
  }
}
</script>

<template>
  <section class="main-section">
    <notifications group="main-errors"/>
    <notifications group="info"/>
    <content-box>
      <p class="fs-16">History Table</p>
      <div class="pag-table">
        <pagination-table
            :array="this.resultArray"
            :content-per-page="4"
            :max-page-show="2"
            :headers="this.headersArray"
        />
      </div>
    </content-box>
    <content-box>
      <div id="calculator"
           @click="calculateCordsByGraphClick($event); drawDotByAreaHit(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue);  addDotsByGraphClick(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue)">
      </div>
    </content-box>
    <content-box>
      <p class="fs-16">Input Form</p>
      <form @submit.prevent class="main-form">
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
</style>