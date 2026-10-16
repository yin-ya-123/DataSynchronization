export default {
    containData(data, dataS) {
        for (let i = 0; i < dataS.length; i++) {
            if (data === dataS[i]) {
                return true
            }
        }
        return false
    },
    /**
     * 判断数据是否在数组中
     * @param data 源 数据
     * @param field 字段
     * @param dataS 目标数据
     * @returns {boolean}
     */
    containDataField(data,field,dataS) {
        for (let i = 0; i < dataS.length; i++) {
            if (data[field] === dataS[i][field]) {
                return true
            }
        }
        return false
    }
}