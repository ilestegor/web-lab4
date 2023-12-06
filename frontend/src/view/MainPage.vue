<script>


import PaginationTable from "@/component/PaginationTable.vue";
import ContentBox from "@/component/ContentBox.vue";
import CustomInput from "@/component/CustomInput.vue";
import MyButton from "@/component/Button.vue";

export default {
  name: "MainPage",
  components: {MyButton, CustomInput, ContentBox, PaginationTable},
  data(){
    return{
      mock: [
        {x:1, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:2, y:3, r:4, exec:233421, cur:"fdsa", hit:true},
        {x:3, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:4, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:5, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:6, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:7, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:8, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:9, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:10, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:11, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:12, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:13, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
        {x:321, y:2, r:3, exec:233421, cur:"fdsa", hit:true},
      ],
      headersArray: [
        {label: "x", value:"X"},
        {label: "y", value:"Y"},
        {label: "r", value:"R"},
        {label: "exec", value:"Execution Time"},
        {label: "cur", value:"Current Time"},
        {label: "hit", value:"Hit Type"},
      ]
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
      <div id="calculator">
      </div>
    </content-box>
    <content-box>
      <p class="fs-16">Input Form</p>
      <form @submit.prevent class="main-form">
        <custom-input label="X value" class="main-input" placeholder-text="X value"></custom-input>
        <custom-input label="Y value" placeholder-text="Y value"></custom-input>
        <custom-input label="R value" placeholder-text="R value"></custom-input>
        <div class="form-buttons">
          <my-button>Send</my-button>
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
</style>