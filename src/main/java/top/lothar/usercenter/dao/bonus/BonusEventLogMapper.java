package top.lothar.usercenter.dao.bonus;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import top.lothar.usercenter.domain.entity.bonus.BonusEventLog;

@Repository
public interface BonusEventLogMapper extends Mapper<BonusEventLog> {
}