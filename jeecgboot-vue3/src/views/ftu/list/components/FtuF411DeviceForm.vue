<template>
    <BasicForm @register="registerForm" name="FtuF411DeviceForm" class="basic-modal-form"/>
</template>
<script lang="ts">
    import {defineComponent} from 'vue';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import {ftuF411DeviceFormSchema} from '../FtuDevice.data';
    import {defHttp} from '/@/utils/http/axios';
    import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils'

    export default defineComponent({
        name:"FtuF411DeviceForm",
        components: {BasicForm},
        emits:['register'],
        props:{
            disabled: {
                type: Boolean,
                default: false
            }
        },
        setup(props,{emit}) {
            const [registerForm, { setProps, resetFields, setFieldsValue, getFieldsValue, validate, scrollToField }] = useForm({
                schemas: ftuF411DeviceFormSchema,
                showActionButtonGroup: false,
                baseColProps: {span: 12}
            });
            /**
            *初始化加载数据
            */
            function initFormData(url,id){
                if(id){
                     defHttp.get({url,params:{id}},{isTransformResponse:false}).then(res=>{
                       res.success && setFieldsValue({...res.result[0]});
                    })
                }
                setProps({disabled: props.disabled})
            }
           /**
            *获取表单数据
            */
           function getFormData(){
             let formData = getFieldsValue();

             // 处理数组字段
             Object.keys(formData).map(k=>{
               if(formData[k] instanceof Array){
                 formData[k] = formData[k].join(',')
               }
             });

             // 检查表单数据是否为空对象
             const isEmpty = Object.keys(formData).every(key =>
               formData[key] === undefined ||
               formData[key] === null ||
               formData[key] === ''
             );

             // 如果为空则返回空数组，否则返回包含数据的数组
             return isEmpty ? [] : [formData];
           }
            /**
            *表单校验
            */
            function validateForm(index){
                return new Promise((resolve, reject) => {
                    // 验证子表表单
                    validate().then(()=>{
                        return resolve()
                    }).catch(({ errorFields }) => {
                      return reject({ error: VALIDATE_FAILED, index, errorFields: errorFields, scrollToField: scrollToField });
                    });
                })
            }
            return {
                registerForm,
                resetFields,
                initFormData,
                getFormData,
                validateForm
            }
        }
    })
</script>
<style lang="less" scoped>
  .basic-modal-form {
    overflow: auto;
    height: 340px;
  }
</style>
