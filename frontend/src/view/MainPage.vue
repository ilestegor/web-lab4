<script>


import PaginationTable from "@/component/PaginationTable.vue";
import useVuelidate from "@vuelidate/core";
import {required, helpers, maxValue, between} from "@vuelidate/validators";
const RED_COLOR = "#00fff7";
const GREEN_COLOR = "#aa00ff"
export default {
  name: "MainPage",
  components: {PaginationTable},
  data(){
    return{
      mock: [
      ],
      headersArray: [
        {label: "x", value:"X"},
        {label: "y", value:"Y"},
        {label: "r", value:"R"},
        {label: "exec", value:"Execution Time"},
        {label: "cur", value:"Current Time"},
        {label: "hit", value:"Hit Type"},
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
      COLORS:{
        RED_COLOR,
        GREEN_COLOR
      },
      localDots: []
    }
  },
  validations(){
    return {
      xValue: {required, between: between(-5, 5)},
      yValue: {required, between: between(-5, 3)},
      rValue: {required, between: between(-5, 5)}
    }
  },
  mounted() {
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
  },
  methods:{
    drawGraphByR(r){
      r = +r
      if (isNaN(Number(r)) || !this.validateR(r)) r = 0
      if (!isNaN(Number(r))){
        this.calculator.setExpression({
          id: 'area-1',
          latex: 'y\\le ' + r + '\\left\\{0\\le-x\\le ' + r  + '\\right\\}\\left\\{y\\ge0\\right\\}'
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
    drawDots(x, y, color){
      if (x !== null && y !== null){
        this.calculator.setExpression({
          id: x + '' + y,
          color: color,
          latex: '\\left(' + x + ',' + y + '\\right)',
        })
      }
    },
    checkArea(x, y, r){
      if (x === "" || y === "" || r === "")return;
      if (y <= r && (-x>=0) && (r >= -x)&& (y>=0))
        return true;
      if (y >= -x-(r/2) && (x <= 0) && (y <= 0))
        return true
      return (x * x + y * y <= r * r) && (x >= 0) && (y <= 0);

    },
    drawDotByAreaHit(x, y, r){
      const areaHit = this.checkArea(x, y, r);
      if (areaHit === undefined){
        return;
      }
      if (areaHit){
        this.drawDots(x, y, this.COLORS.GREEN_COLOR)
      } else this.drawDots(x, y, this.COLORS.RED_COLOR)

    },
    addDotsToArray(x, y, r){
      if (this.validateX(x) && this.validateY(y) && this.validateR(r)){
        const dots = {x: x, y: y, r: r}
        this.localDots.push(dots)
        console.log(this.localDots)
      }
    },
    calculateCordsByGraphClick(e){
      const calcBounderies = this.calcElement.getBoundingClientRect();
      const x = e.clientX - calcBounderies.left;
      const y = e.clientY - calcBounderies.top;
      this.coordinatesFromGraph = this.calculator.pixelsToMath({x: x, y: y});
    },
    changeDotsColorByR(){
      if (this.rValue !== ""){
        this.localDots.forEach(dots => this.drawDotByAreaHit(dots.x, dots.y, this.rValue))
      } else {
        this.localDots.forEach(dots => this.drawDots(dots.x, dots.y, this.COLORS.RED_COLOR))
      }
    },
    validateX(x){
      return x >= -5 && x <= 5 && x !== ""
    },
    validateY(y){
      return y>=-5 && y <= 3 && y !== "";
    },
    validateR(r){
      return r >= -5 && r <= 5 && r !== "";
    }
  }
}
</script>

<template>
  <section class="main-section">
    <content-box>
      <p class="fs-16">History Table</p>
      <div class="pag-table">
        <pagination-table
        :array="this.mock"
        :content-per-page="4"
        :max-page-show="2"
        :headers="this.headersArray"
        />
      </div>
    </content-box>
    <content-box>
      <div id="calculator" @click="calculateCordsByGraphClick($event); drawDotByAreaHit(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue); addDotsToArray(this.coordinatesFromGraph.x, this.coordinatesFromGraph.y, this.rValue)">
      </div>
    </content-box>
    <content-box>
      <p class="fs-16">Input Form</p>
      <form @submit.prevent  class="main-form">
        <div class="main-form-input-wrapper">
          <custom-input label="X value" class="main-input" placeholder-text="Enter value from -5...5" v-model:input-value="xValue" @input="this.v1$.$touch()">
          </custom-input>
          <p class="error-message" v-for="error2 in v1$.xValue.$errors" :key="error2.$uid">{{error2.$message}}</p>
        </div>
        <div class="main-form-input-wrapper">
          <custom-input label="Y value" placeholder-text="Enter value from -5..3" v-model:input-value.trim="yValue" @input="this.v2$.$touch()"></custom-input>
          <p class="error-message" v-for="error2 in v2$.yValue.$errors" :key="error2.$uid">{{error2.$message}}</p>
        </div>
        <div class="main-form-input-wrapper">
          <custom-input label="R value" placeholder-text="Enter value from -5...5" @input="drawGraphByR(this.rValue); changeDotsColorByR(); this.v3$.$touch()" v-model:input-value.trim="rValue"></custom-input>
          <p class="error-message" v-for="error3 in v3$.rValue.$errors" :key="error3.$uid">{{error3.$message}}</p>
        </div>
        <div class="form-buttons">
          <my-button @click="drawDotByAreaHit(this.xValue, this.yValue, this.rValue); addDotsToArray(this.xValue, this.yValue, this.rValue)">Send</my-button>
          <my-button>Clear</my-button>
        </div>
      </form>
    </content-box>
  </section>
</template>

<style>
.main-section{
  display: flex;
  justify-content: center;
  gap: 10px;
  width: 100%;
  margin-top: 20px;
}
.pag-table{
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.fs-16{
  font-size: 16px;
}
#calculator{
  width: 100%;
  max-width: 600px;
  min-width: 200px;
  height: 100%;
}
.form-buttons{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  gap: 5px;
}
.form-buttons > button{
  width: 46%;
}
.main-form > div{
  margin-top: 25px;
}
.input-wrap > label{
  margin-right: 10px;
}
.input-wrap > input{
  width: 70%;
}
.error-message{
  margin-top: 5px;
  font-size: 11px;
  color: #e54545;
}
</style>