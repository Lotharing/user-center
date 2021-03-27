package top.lothar.usercenter.dao.user;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import top.lothar.usercenter.domain.entity.user.User;

@Repository
public interface UserMapper extends Mapper<User> {
}