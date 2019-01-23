package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Domain;
import com.galen.program.core.domain.Path;
import com.galen.program.core.domain.Entity;
import com.galen.program.core.domain.Identity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class BaseDomain extends BaseEntity implements Domain {
    private Map<Identity,Entity> entityMap = new HashMap<Identity, Entity>();
    private Map<Identity,Domain> refDomainMap = new HashMap<Identity, Domain>();

    @Override
    public void addEntity(Entity entity) {
        entityMap.put(entity.getIdentity(),entity);
    }

    @Override
    public Entity getEntity(Identity identity) {
        return entityMap.get(identity);
    }

    @Override
    public Set<Identity> getIdentities() {
        return entityMap.keySet();
    }

    @Override
    public Entity getEntity(Path address) {
        return route(address).getEntity(address.last());
    }

    @Override
    public Domain addRefDomain(Domain domain) {
        return refDomainMap.put(domain.getIdentity(),domain);
    }

    @Override
    public Domain getRefDomain(Identity identity) {
        return refDomainMap.get(identity);
    }

    @Override
    public Set<Identity> getRefDomains() {
        return refDomainMap.keySet();
    }

    @Override
    public Domain getRefDomain(Path address) {
        return getRefDomain(address.last());
    }

    @Override
    public Domain rootDomain() {
        return null;
    }

    @Override
    public Domain route(Path address) {
        return null;
    }

    @Override
    public Path getAddress() {
        return null;
    }
}
