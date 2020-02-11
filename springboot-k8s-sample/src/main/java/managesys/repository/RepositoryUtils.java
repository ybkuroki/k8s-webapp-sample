package managesys.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

public abstract class RepositoryUtils {

    /** 指定したクラスのエンティティ情報を返す */
    @SuppressWarnings("unchecked")
    public static <T> JpaEntityInformation<T, Serializable> entityInformation(EntityManager em, Class<T> clazz) {
        return (JpaEntityInformation<T, Serializable>) JpaEntityInformationSupport.getEntityInformation(clazz, em);
    }

}
