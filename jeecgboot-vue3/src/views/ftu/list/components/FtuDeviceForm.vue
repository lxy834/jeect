<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
      <a-tab-pane tab="通信终端" key="ftuF411Device" :forceRender="true">
        <FtuF411DeviceForm ref="ftuF411DeviceForm" :disabled="formDisabled"></FtuF411DeviceForm>
      </a-tab-pane>
      <a-tab-pane tab="电气量信息" key="ftuElectlVolume" :forceRender="true">
        <JVxeTable
          keep-source
          resizable
          ref="ftuElectlVolume"
          v-if="ftuElectlVolumeTable.show"
          :loading="ftuElectlVolumeTable.loading"
          :columns="ftuElectlVolumeTable.columns"
          :dataSource="ftuElectlVolumeTable.dataSource"
          :height="340"
          :rowNumber="true"
          :rowSelection="true"
          :disabled="formDisabled"
          :toolbar="true"
        />
      </a-tab-pane>
      <a-tab-pane tab="告警信息" key="ftuWarnInfo" :forceRender="true">
        <JVxeTable
          keep-source
          resizable
          ref="ftuWarnInfo"
          v-if="ftuWarnInfoTable.show"
          :loading="ftuWarnInfoTable.loading"
          :columns="ftuWarnInfoTable.columns"
          :dataSource="ftuWarnInfoTable.dataSource"
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
  import FtuF411DeviceForm from './FtuF411DeviceForm.vue'
  import {getBpmFormSchema,ftuElectlVolumeColumns,ftuWarnInfoColumns} from '../FtuDevice.data';
  import {saveOrUpdate,ftuF411DeviceList,ftuElectlVolumeList,ftuWarnInfoList} from '../FtuDevice.api';

  export default defineComponent({
    name: "FtuDeviceForm",
    components:{
      BasicForm,
      FtuF411DeviceForm,
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

      const refKeys = ref(['ftuF411Device', 'ftuElectlVolume', 'ftuWarnInfo', ]);
      const activeKey = ref('ftuF411Device');
      const ftuF411DeviceForm = ref();
      const ftuElectlVolume = ref();
      const ftuWarnInfo = ref();
      const tableRefs = {ftuElectlVolume, ftuWarnInfo, };
      const ftuElectlVolumeTable = reactive({
        loading: false,
        dataSource: [],
        columns:ftuElectlVolumeColumns,
        show: false
      })
      const ftuWarnInfoTable = reactive({
        loading: false,
        dataSource: [],
        columns:ftuWarnInfoColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          ftuF411DeviceList: ftuF411DeviceForm.value.getFormData(),
          ftuElectlVolumeList: allValues.tablesValue[0].tableData,
          ftuWarnInfoList: allValues.tablesValue[1].tableData,
        }
      }
      //校验所有一对一子表表单
      function validateSubForm(allValues){
        return new Promise((resolve, _reject)=>{
          Promise.all([
            ftuF411DeviceForm.value.validateForm(0),
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

      const queryByIdUrl = '/ftu/ftuDevice/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        ftuF411DeviceForm.value.initFormData(ftuF411DeviceList, data.id);
        requestSubTableData(ftuElectlVolumeList, {id: data.id}, ftuElectlVolumeTable, ()=>{
          ftuElectlVolumeTable.show = true;
        });
        requestSubTableData(ftuWarnInfoList, {id: data.id}, ftuWarnInfoTable, ()=>{
          ftuWarnInfoTable.show = true;
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
        ftuF411DeviceForm,
        ftuElectlVolume,
        ftuWarnInfo,
        ftuElectlVolumeTable,
        ftuWarnInfoTable,
      }
    }
  });
</script>