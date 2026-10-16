import {ElMessage} from "element-plus";

export default {
    isEmpty(str) {
        if ((str === undefined || str === '' || str === null)) {
            ElMessage.error("请先选中数据后");
            return false;
        } else {
            return true;
        }
    },
    strIsEmpty(str) {
        return ((str === undefined || str === '' || str === null || str.trim().length===0));
    },

    /**
     * 解析SQL语句中的 #{} 占位符
     * @param {string} sql - 包含占位符的SQL语句
     * @param {boolean} showMessage - 是否显示提示消息，默认为false
     * @returns {Object} 包含占位符总数和占位符名称数组的对象
     */
    parsePlaceholders(sql, showMessage = false) {
        if (this.strIsEmpty(sql)) {
            if (showMessage) {
                ElMessage.warning("SQL语句为空");
            }
            return {
                total: 0,
                placeholders: []
            };
        }

        // 使用正则表达式匹配 #{} 占位符
        const regex = /(?<!\\)#\{([^}]*)\}/g;
        const placeholders = [];
        let match;

        // 收集所有匹配的占位符
        while ((match = regex.exec(sql)) !== null) {
            const name = match[1].trim();
            if (name) {
                placeholders.push(name);
            }
        }

        const result = {
            total: placeholders.length,
            placeholders: placeholders
        };

        if (showMessage) {
            ElMessage.success(`共找到 ${result.total} 个占位符`);
        }

        return result;
    },

    /**
     * 增强版：解析SQL语句中的 #{} 占位符，包含详细位置信息
     * @param {string} sql - 包含占位符的SQL语句
     * @param {boolean} showMessage - 是否显示提示消息，默认为false
     * @returns {Object} 包含占位符总数、占位符数组和详细信息
     */
    parsePlaceholdersAdvanced(sql, showMessage = false) {
        if (this.strIsEmpty(sql)) {
            if (showMessage) {
                ElMessage.warning("SQL语句为空");
            }
            return {
                total: 0,
                placeholders: [],
                details: []
            };
        }

        const regex = /(?<!\\)#\{([^}]*)\}/g;
        const placeholders = [];
        const details = [];
        let match;

        while ((match = regex.exec(sql)) !== null) {
            const name = match[1].trim();
            if (name) {
                placeholders.push(name);
                details.push({
                    name: name,
                    index: match.index,
                    raw: match[0],
                    length: match[0].length
                });
            }
        }

        const result = {
            total: placeholders.length,
            placeholders: placeholders,
            details: details
        };

        if (showMessage) {
            ElMessage.success(`共找到 ${result.total} 个占位符`);
        }

        return result;
    },

    /**
     * 获取去重后的占位符列表
     * @param {string} sql - 包含占位符的SQL语句
     * @returns {Array} 去重后的占位符数组
     */
    getUniquePlaceholders(sql) {
        const result = this.parsePlaceholders(sql);
        return [...new Set(result.placeholders)];
    },

}