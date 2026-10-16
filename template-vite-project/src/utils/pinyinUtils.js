import pinyin from "pinyin";

export default {
    getPinyinFirst(str) {
        let pym = ''
        let a = pinyin(str, {
            style: pinyin.STYLE_FIRST_LETTER  // 设置拼音风格为首字母
        }).map(item => item[0].toUpperCase());  // 获取首字母并转换为大写
        for (var str2 in a) {
            pym += a[str2]
        }
        return pym
    }
}