package com.galen.program.core.domain;

import java.util.Set;

/**
 * 域
 * 同时是实体
 * Created by baogen.zhang on 2018/11/24
 *
 * @author baogen.zhang
 * @date 2018/11/24
 */
public interface Domain extends Entity {

    /**
     * 添加实体，以添加的实体不能重复添加。
     *
     * @param entity
     * @return
     */
    void addEntity(Entity entity);

    /**
     * 返回暴露出的内部实体,如果不存在就创建域对象并标记为内部域。
     *
     * @param identity
     * @return
     */
    Entity getEntity(Identity identity);

    Set<Identity> getIdentities();

    Entity getEntity(Path path);

    /**
     * 引用该域的域
     *
     * @param domain
     * @return
     */
    Domain addRefDomain(Domain domain);

    Domain getRefDomain(Identity identity);

    Set<Identity> getRefDomains();

    Domain rootDomain();

    Domain route(Path path);



    //TODO 构建生命周期管理

}
