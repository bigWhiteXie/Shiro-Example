package com.codexie.mapper;

import com.codexie.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    @Select("select * from user where uname=#{param1}")
    User selUserMapper(String uname);

    @Select("select rname from t_role \n" +
            "where\n" +
            "\trid in (\n" +
            "\t\tselect rid from r_user \n" +
            "\t\twhere uid=\n" +
            "\t\t\t(select id from user where uname=#{param1})\n" +
            ")")
    List<String> selRolesMapper(String pricipal);

    @Select("select pname from t_permission\n" +
            "\twhere pid in \n" +
            "\t(select pid from p_role where\n" +
            "\trid in \n" +
            "\t(\n" +
            "\t\tselect rid from t_role \n" +
            "\t\twhere\n" +
            "\t\t\trid in (\n" +
            "\t\t\t\tselect rid from r_user \n" +
            "\t\t\t\twhere uid=\n" +
            "\t\t\t\t\t(select id from user where uname=#{param1})\n" +
            "\t\t)\n" +
            "\t)\n" +
            ")")
    List<String> selPsMapper(String pricipal);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}