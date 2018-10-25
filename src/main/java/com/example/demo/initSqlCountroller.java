package com.example.demo;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by ThinkPad on 2018-10-24.
 */
@RestController
@EnableAutoConfiguration
public class initSqlCountroller {

    @RequestMapping(value = "/initSql",  method = RequestMethod.POST)
    public List<String> initSql(HttpServletRequest request, String tableName){
        List<String> sql = new ArrayList<String>();

        String a = "";
        Map<String, String[]> map = request.getParameterMap();
        Set<String> keySet = map.keySet();
        List<String> colums = new ArrayList<>();

        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();){
            String b = iterator.next();
            if ("tableName".equals(b)){
                continue;
            }
            colums.add(b);
//            a += b;
        }

        StringBuffer insert1 = new StringBuffer("insert into ");
        StringBuffer insert2 = new StringBuffer("\n</trim>\n" + " <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >\n" + "\n" + " Sys_guid(), ");
        insert1.append(tableName+" ");
        insert1.append("\n<trim prefix=\"(GKEY,\" suffix=\")\" suffixOverrides=\",\" > ");

        StringBuffer update = new StringBuffer("update ");
        update.append(tableName+" set ");
        update.append("\n<trim prefix=\"\" suffix=\" where GKEY = #{GKEY}\" suffixOverrides=\",\">");


        for (int i=0;i<colums.size();i++){
            insert1.append("\n<if test='"+colums.get(i)+" != null and "+colums.get(i)+" != \"\" '>\n"+colums.get(i)+",\n</if> ");
            insert2.append("\n<if test='"+colums.get(i)+" != null and "+colums.get(i)+" != \"\" '>\n#{"+colums.get(i)+"},\n</if>");

            update.append("\n<if test='"+colums.get(i)+" != null and "+colums.get(i)+" != \"\" '>\n"+colums.get(i)+"="+"#{"+colums.get(i)+"},\n</if>");
        }
        a = insert1.append(insert2).append("\n</trim>\n\n\n").toString();
        update.append("\n</trim>\n\n\n");

        StringBuffer select = new StringBuffer("select * from ");
        select.append(tableName+"\n\n\n");
        StringBuffer delete = new StringBuffer("delete from ");
        delete.append(tableName+" where GKEY = #{GKEY}"+"\n\n\n");
//        sql.put("insert",a);
//        sql.put("update",update.toString());
//        sql.put("select",select.toString());
//        sql.put("delete",delete.toString());
//        sql.add(a);
//        sql.add(update.toString());
//        sql.add(select.toString());
//        sql.add(delete.toString());
        try {
            File file = new File("D://tsg-sql//"+tableName+".sql");
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fileWriter);
            System.out.println(a);
            bf.write(a);
            bf.write(update.toString());
            bf.write(select.toString());
            bf.write(delete.toString());
            bf.close();
        }catch (Exception e){
            e.printStackTrace();
        }



        return sql;
    }

}
