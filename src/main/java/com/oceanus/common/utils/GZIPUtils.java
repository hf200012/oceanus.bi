package com.oceanus.common.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtils {
    private static final String EN_CODING = "utf-8";

    /**压缩*/
    public static String compress(String dataMsg){

        try {
            //1.base64编码 二进制转义
            byte[] base64Str = Base64.getEncoder().encode(dataMsg.getBytes(EN_CODING));
            //2.zip压缩
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            GZIPOutputStream zipOs = new GZIPOutputStream(baOs, true);
            zipOs.write(base64Str);
            zipOs.flush();
            zipOs.close();//关闭流 输出缓存区内容
            //3.获取并转义压缩内容
            return Base64.getEncoder().encodeToString(baOs.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("GZipUtil.compress 压缩异常", e);
        }
    }

    /**解压缩*/
    public static String unCompress(String dataMsg){

        //1.转义二进制
        byte[] base64Bytes = Base64.getDecoder().decode(dataMsg.getBytes());

        try {
            //1.zip解压缩
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            ByteArrayInputStream baIs = new ByteArrayInputStream(base64Bytes);
            GZIPInputStream zipIs = new GZIPInputStream(baIs);

            byte[] temp = new byte[256];
            int n;
            while ((n = zipIs.read(temp)) >= 0){
                baOs.write(temp, 0, n);
            }

            //3.base64 二进制恢复
            byte[] originMsg = Base64.getDecoder().decode(baOs.toByteArray());
            return new String(originMsg);
        } catch (IOException e) {
            throw new RuntimeException("GZipUtil.unCompress 解压缩异常", e);
        }
    }

    public static void main(String[] args) {
        String a ="{\"extendDsList\":[{\"datasource\":{\"code\":\"first_internal_1\",\"createTime\":\"2019-10-09 14:58:21\",\"description\":\"行内第一数据源\",\"modifyTime\":\"2019-10-09 16:55:25\",\"name\":\"行内第一数据源\",\"providerCode\":\"internal_database\",\"providerInfo\":\"{\\\"jdbcUrl\\\":\\\"jdbc:mysql://10.2.1.106:3306/data_engine\\\",\\\"password\\\":\\\"Y1R1MTIzNDU2\\\",\\\"userName\\\":\\\"ctu\\\"}\",\"providerName\":\"数据聚合联调库\",\"providerType\":\"MYSQL\",\"querySql\":\"select code, name, url, source from name_list_info where code = #{code}\",\"serviceTtl\":7,\"serviceUrl\":\"\",\"status\":1,\"type\":4},\"paramReqList\":[{\"code\":\"name\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:30\",\"name\":\"名称\",\"required\":1},{\"code\":\"id\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"long\",\"modifyTime\":\"2019-10-09 15:01:34\",\"name\":\"身份证号\",\"required\":1}],\"paramResList\":[{\"code\":\"phone\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"int\",\"modifyTime\":\"2019-10-09 15:01:24\",\"name\":\"手机号\"},{\"code\":\"rank\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:19\",\"name\":\"排位分\"}],\"provider\":{\"code\":\"internal_database\",\"createTime\":\"2019-10-09 14:58:09\",\"modifyTime\":\"2019-10-09 14:58:09\",\"name\":\"数据聚合联调库\"}}],\"realDsList\":[]}";
        String compressStr = compress(a);
        System.out.println("压缩前大小：" + a.length());
        System.out.println("压缩后大小：" + compressStr.length());
        System.out.println(unCompress(compress(a)));
    }

}
