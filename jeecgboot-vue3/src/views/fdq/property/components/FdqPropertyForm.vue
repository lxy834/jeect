<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
      <a-tab-pane tab="智慧终端" key="fdqBasic" :forceRender="true">
        <FdqBasicForm ref="fdqBasicForm" :disabled="formDisabled"></FdqBasicForm>
      </a-tab-pane>
      <a-tab-pane tab="历史数据" key="fdqController" :forceRender="true">
        <JVxeTable
          keep-source
          resizable
          ref="fdqController"
          v-if="fdqControllerTable.show"
          :loading="fdqControllerTable.loading"
          :columns="fdqControllerTable.columns"
          :dataSource="fdqControllerTable.dataSource"
          :height="340"
          :rowNumber="true"
          :rowSelection="true"
          :disabled="formDisabled"
          :toolbar="true"
        />
      </a-tab-pane>
    </a-tabs>

    <div style="width: 100%;text-align: center" v-if="!formDisabled">
      <a-button @click="handleSubmit" pre-icon="ant-design:check" type="primary">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts">

  import {BasicForm, useForm} from '/@/components/Form/index';
  import { computed, defineComponent, reactive, ref, unref } from 'vue';
  import {defHttp} from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import FdqBasicForm from './FdqBasicForm.vue'
  import {getBpmFormSchema,fdqControllerColumns} from '../FdqProperty.data';
  import {saveOrUpdate,fdqBasicList,fdqControllerList} from '../FdqProperty.api';

  export default defineComponent({
    name: "FdqPropertyForm",
    components:{
      BasicForm,
      FdqBasicForm,
    },
    props:{
      formData: propTypes.object.def({}),
      formBpm: propTypes.bool.def(true),
    },
    setup(props){
      const [registerForm, { setFieldsValue, setProps }] = useForm({
        labelWidth: 150,
        schemas: getBpmFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: {span: 12}
      });

      const formDisabled = computed(()=>{
        if(props.formData.disabled === false){
          return false;
        }
        return true;
      });

      const refKeys = ref(['fdqBasic', 'fdqController', ]);
      const activeKey = ref('fdqBasic');
      const fdqBasicForm = ref();
      const fdqController = ref();
      const tableRefs = {fdqController, };
      const fdqControllerTable = reactive({
        loading: false,
        dataSource: [],
        columns:fdqControllerColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          fdqBasicList: fdqBasicForm.value.getFormData(),
          fdqControllerList: allValues.tablesValue[0].tableData,
        }
      }
      //校验所有一对一子表表单
      function validateSubForm(allValues){
        return new Promise((resolve, _reject)=>{
          Promise.all([
            fdqBasicForm.value.validateForm(0),
          ]).then(() => {
            resolve(allValues)
          }).catch(e => {
            if (e.error === VALIDATE_FAILED) {
              // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
              activeKey.value = e.index == null ? unref(activeKey) : refKeys.value[e.index]
            } else {
              console.error(e)
            }
          })
        })
      }

      //表单提交事件
      async function requestAddOrEdit(values) {
        await saveOrUpdate(values, true);
      }

      const queryByIdUrl = '/generate/fdqProperty/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        fdqBasicForm.value.initFormData(fdqBasicList, data.id);
        requestSubTableData(fdqControllerList, {id: data.id}, fdqControllerTable, ()=>{
          fdqControllerTable.show = true;
        });
        //默认是禁用
        await setProps({disabled: formDisabled.value})
      }

      initFormData();

      return {
        registerForm,
        formDisabled,
        formRef,
        handleSubmit,
        activeKey,
        handleChangeTabs,
        fdqBasicForm,
        fdqController,
        fdqControllerTable,
      }
    }
  });
</script>
