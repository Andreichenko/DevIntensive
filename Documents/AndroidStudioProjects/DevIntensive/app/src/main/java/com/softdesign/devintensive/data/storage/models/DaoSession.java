package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserModelRes;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/**
 * Created by AlexFrei on 20.07.16.
 */
public class DaoSession extends AbstractDaoSession {
    private final DaoConfig repositoryDaoConfig;
    private final DaoConfig userDaoConfig;
    private final RepositoryDao repositoryDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        repositoryDaoConfig = daoConfigMap.get(RepositoryDao.class).clone();
        repositoryDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        repositoryDao = new RepositoryDao(repositoryDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Repository.class, repositoryDao);
        registerDao(UserModelRes.User.class, userDao);
    }

    public void clear() {
        repositoryDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public RepositoryDao getRepositoryDao() {
        return repositoryDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
