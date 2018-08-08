package com.mystic.login;

import org.springframework.stereotype.Component;

import com.mystic.common.MySqlCommonDao;

@Component
public class MySqlLoginDao extends MySqlCommonDao implements LoginDao {

}
