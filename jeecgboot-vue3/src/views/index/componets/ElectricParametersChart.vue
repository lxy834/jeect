<template>
  <div class="container">
    <h1>电气参数随时间变化趋势图</h1>

    <div class="legend">
      <div class="legend-item" v-for="(item, index) in legendItems" :key="index">
        <div class="legend-marker" :style="{ backgroundColor: item.color }"></div>
        <span>{{ item.name }}</span>
      </div>
    </div>

    <div class="chart-container" ref="chartRef"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'ElectricParametersChart',
  data() {
    return {
      myChart: null,
      legendItems: [
        { name: '电压 (V)', color: '#5793f3' },
        { name: '电流 (A)', color: '#d14a61' },
        { name: '有功功率 (kW)', color: '#675bba' },
        { name: '功率因数', color: '#ff7f50' }
      ],
      chartData: {
        dates: [],
        voltage: [],
        current: [],
        power: [],
        powerFactor: []
      }
    };
  },
  mounted() {
    this.generateElectricData();
    this.initChart();
    // window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeChart);
    if (this.myChart) {
      this.myChart.dispose();
    }
  },
  methods: {
    // 生成电气参数模拟数据
    generateElectricData() {
      const dates = [];
      const voltage = [];
      const current = [];
      const power = [];
      const powerFactor = [];

      // 从今天开始向前生成30天的数据
      const today = new Date();

      for (let i = 30; i >= 0; i--) {
        const date = new Date(today);
        date.setDate(today.getDate() - i);
        const dateStr = date.toISOString().split('T')[0];
        dates.push(dateStr);

        // 生成模拟数据
        const baseVoltage = 220;
        const baseCurrent = 10;
        const basePower = baseVoltage * baseCurrent * 0.85 / 1000; // kW

        // 添加随机波动
        voltage.push(
          Math.round((baseVoltage + (Math.random() * 10 - 5)) * 10) / 10
        );
        current.push(
          Math.round((baseCurrent + (Math.random() * 3 - 1.5)) * 100) / 100
        );
        power.push(
          Math.round((basePower + (Math.random() * 0.5 - 0.25)) * 100) / 100
        );
        // 功率因数范围0.8-0.95
        powerFactor.push(
          Math.round((0.85 + (Math.random() * 0.1 - 0.05)) * 100) / 100
        );
      }

      this.chartData = { dates, voltage, current, power, powerFactor };
    },

    // 初始化图表
    initChart() {
      this.myChart = echarts.init(this.$refs.chartRef);
      var option;

      const data = [["2000-06-05", 116], ["2000-06-06", 129], ["2000-06-07", 135], ["2000-06-08", 86], ["2000-06-09", 73], ["2000-06-10", 85], ["2000-06-11", 73], ["2000-06-12", 68], ["2000-06-13", 92], ["2000-06-14", 130], ["2000-06-15", 245], ["2000-06-16", 139], ["2000-06-17", 115], ["2000-06-18", 111], ["2000-06-19", 309], ["2000-06-20", 206], ["2000-06-21", 137], ["2000-06-22", 128], ["2000-06-23", 85], ["2000-06-24", 94], ["2000-06-25", 71], ["2000-06-26", 106], ["2000-06-27", 84], ["2000-06-28", 93], ["2000-06-29", 85], ["2000-06-30", 73], ["2000-07-01", 83], ["2000-07-02", 125], ["2000-07-03", 107], ["2000-07-04", 82], ["2000-07-05", 44], ["2000-07-06", 72], ["2000-07-07", 106], ["2000-07-08", 107], ["2000-07-09", 66], ["2000-07-10", 91], ["2000-07-11", 92], ["2000-07-12", 113], ["2000-07-13", 107], ["2000-07-14", 131], ["2000-07-15", 111], ["2000-07-16", 64], ["2000-07-17", 69], ["2000-07-18", 88], ["2000-07-19", 77], ["2000-07-20", 83], ["2000-07-21", 111], ["2000-07-22", 57], ["2000-07-23", 55], ["2000-07-24", 60]];
      const dateList = data.map(function (item) {
        return item[0];
      });
      const valueList = data.map(function (item) {
        return item[1];
      });
      option = {
        // Make gradient line here
        visualMap: [
          {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 400
          },
          {
            show: false,
            type: 'continuous',
            seriesIndex: 1,
            dimension: 0,
            min: 0,
            max: dateList.length - 1
          }
        ],
        title: [
          {
            left: 'center',
            text: 'Gradient along the y axis'
          },
          {
            top: '55%',
            left: 'center',
            text: 'Gradient along the x axis'
          }
        ],
        tooltip: {
          trigger: 'axis'
        },
        xAxis: [
          {
            data: dateList
          },
          {
            data: dateList,
            gridIndex: 1
          }
        ],
        yAxis: [
          {},
          {
            gridIndex: 1
          }
        ],
        grid: [
          {
            bottom: '60%'
          },
          {
            top: '60%'
          }
        ],
        series: [
          {
            type: 'line',
            showSymbol: false,
            data: valueList
          },
          {
            type: 'line',
            showSymbol: false,
            data: valueList,
            xAxisIndex: 1,
            yAxisIndex: 1
          }
        ]
      };
      this.myChart.setOption(option);
    },

    // 图表自适应
    resizeChart() {
      if (this.myChart) {
        this.myChart.resize();
      }
    }
  }
};
</script>

<style scoped>
.container {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 20px;
  background-color: #f5f5f5;
}


.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
}

.legend-marker {
  width: 12px;
  height: 2px;
  margin-right: 5px;
}

.analysis {
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h1, h3 {
  color: #333;
}

ul {
  color: #666;
}
</style>
