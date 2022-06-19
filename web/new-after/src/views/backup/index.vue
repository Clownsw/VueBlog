<template>
    <div class="app-container">
        <el-form :model="backup" :rules="rules" ref="backUpForm" label-width="120px">
            <el-form-item label="数据库用户名" prop="username">
                <el-input v-model="backup.username"></el-input>
            </el-form-item>

            <el-form-item label="数据库密码" prop="password">
                <el-input v-model="backup.password"></el-input>
            </el-form-item>

            <el-form-item label="操作员名称" prop="operator">
                <el-input v-model="backup.operator"></el-input>
            </el-form-item>

            <el-form-item label="操作员密码" prop="operator_password">
                <el-input v-model="backup.operator_password"></el-input>
            </el-form-item>

            <el-form-item label="仓储名称" prop="bucket_name">
                <el-input v-model="backup.bucket_name"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm('backUpForm')">修改</el-button>
                <el-button @click="resetForm('backUpForm')">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import backupApi from '../../api/backup';
export default {
    name: 'BackUpEdit',
    data() {
        return {
            backup: {},
            rules: {
                username: [
                    { required: true, message: '请输入数据库用户名', trigger: 'blur' },
                    { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入数据库密码', trigger: 'blur' },
                    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
                ],
                operator: [
                    { required: true, message: '请输入操作员名称', trigger: 'blur' },
                    { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
                ],
                operator_password: [
                    { required: true, message: '请输入操作员密码', trigger: 'blur' },
                ],
                bucket_name: [
                    { required: true, message: '请输入仓库名称', trigger: 'blur' },
                ],
            }
        }
    },
    created() {
        backupApi.getBackUp().then(resp => {
            this.backup = resp.data
        })
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    backupApi.updateBackUp(this.backup).then(resp => {
                        this.$message.success(resp.message)
                    })
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
</script>

<style scoped>
.el-input {
    width: 400px;
}
</style>