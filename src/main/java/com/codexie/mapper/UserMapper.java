package com.codexie.mapper;

import com.codexie.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select rname from t_role where rid in\n" +
            "(\n" +
            "\tselect DISTINCT rid from r_user where uid =\n" +
            "\t(\n" +
            "\tselect id from user where uname=#{param1}\n" +
            "\t)\n" +
            ")")
    List<String> selRolesMapper(String pricipal);

    @Select("select pname from t_permission where pid in (\n" +
            "\tselect pid from p_role where rid in (\n" +
            "\t\tselect rid from t_role where rid in\n" +
            "\t\t(\n" +
            "\t\t\tselect DISTINCT rid from r_user where uid =\n" +
            "\t\t\t(\n" +
            "\t\t\tselect id from user where uname=#{param1}\n" +
            "\t\t\t)\n" +
            "\t\t))\n" +
            ")\n")
    List<String> selPsMapper(String pricipal);

    @Select("select * from user where uname=#{param1}")
    User selUserMapper(String uname);
}
