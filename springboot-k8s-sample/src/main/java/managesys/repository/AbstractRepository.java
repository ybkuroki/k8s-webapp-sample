package managesys.repository;

import static org.springframework.data.jpa.repository.query.QueryUtils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

// reference code: spring-data-jpa/SimpleJpaRepository.java
// @see https://github.com/spring-projects/spring-data-jpa/blob/master/src/main/java/org/springframework/data/jpa/repository/support/SimpleJpaRepository.java
public abstract class AbstractRepository {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    public abstract EntityManager em();

    private CrudMethodMetadata metadata;

    /**
     * Configures a custom {@link CrudMethodMetadata} to be used to detect {@link LockModeType}s and query hints to be
     * applied to queries.
     *
     * @param crudMethodMetadata
     */
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        this.metadata = crudMethodMetadata;
    }

    protected CrudMethodMetadata getRepositoryMethodMetadata() {
        return metadata;
    }

    private <T> String getDeleteAllQueryString(Class<T> clazz) {
        return getQueryString(DELETE_ALL_QUERY_STRING, RepositoryUtils.entityInformation(em(), clazz).getEntityName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
     */
    public <T> void deleteById(Class<T> clazz, Serializable id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        delete(findById(clazz, id).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!", RepositoryUtils.entityInformation(em(), clazz).getJavaType(), id), 1)));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
     */
    public <T> void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        em().remove(em().contains(entity) ? entity : em().merge(entity));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
     */
    public <T> void deleteAll(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (T entity : entities) {
            delete(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#deleteInBatch(java.lang.Iterable)
     */
    public <T> void deleteInBatch(Class<T> clazz, Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        if (!entities.iterator().hasNext()) {
            return;
        }

        applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, RepositoryUtils.entityInformation(em(), clazz).getEntityName()), entities, em())
        .executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.Repository#deleteAll()
     */
    public <T> void deleteAll(Class<T> clazz) {
        for (T element : findAll(clazz)) {
            delete(element);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#deleteAllInBatch()
     */
    public <T> void deleteAllInBatch(Class<T> clazz) {
        em().createQuery(getDeleteAllQueryString(clazz)).executeUpdate();
    }

    /**
     * JPQLによる検索でデータを取得します。
     * @param jpql JPQL文
     * @param clazz     エンティティクラス
     * @param args パラメータ(?1, ?2...で記述)
     * @return 取得したエンティティオブジェクト
     */
    public <T> List<T> find(String jpql, Class<T> clazz, Object... args) {
        return bindArgs(em().createQuery(jpql, clazz), args).getResultList();
    }

    /**
     * JPQLによる検索でデータを1件取得します。
     * @param jpql JPQL文
     * @param clazz     エンティティクラス
     * @param args パラメータ(?1, ?2...で記述)
     * @return 取得したエンティティオブジェクト
     */
    public <T> Optional<T> findOne(String jpql, Class<T> clazz, Object... args) {
        try {
            return Optional.of(bindArgs(em().createQuery(jpql, clazz), args).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * JPQLによる検索でデータを取得します。(ソート利用可)
     * @param jpql JPQL文
     * @param clazz     エンティティクラス
     * @param sort Sortオブジェクト(order by句が追加されます)
     * @param args パラメータ(?1, ?2...で記述)
     * @return 取得したエンティティオブジェクト
     */
    public <T> Page<T> find(String jpql, Class<T> clazz, Sort sort, Object... args) {
        TypedQuery<T> query = bindArgs(em().createQuery(QueryUtils.applySorting(jpql, sort), clazz), args);
        return new PageImpl<T>(query.getResultList());
    }

    /**
     * JPQLによる検索でデータを取得します。(ページング利用可)
     * @param jpql JPQL文
     * @param clazz     エンティティクラス
     * @param pageable ページングオブジェクト(ソートも反映されます)
     * @param args パラメータ(?1, ?2...で記述)
     * @return 取得したエンティティオブジェクト
     */
    public <T> Page<T> find(String jpql, Class<T> clazz, Pageable pageable, Object... args) {
        TypedQuery<T> query = bindArgs(em().createQuery(QueryUtils.applySorting(jpql, pageable.getSort()), clazz), args);
        return isUnpaged(pageable) ? new PageImpl<T>(query.getResultList())
                : readPage(query, clazz, pageable, null);
    }

    /**
     * クエリのパラメータに値を紐づけます。
     * @param query クエリ
     * @param args 値
     * @return 値が紐づいたクエリ
     */
    public <T> TypedQuery<T> bindArgs(TypedQuery<T> query, Object... args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i + 1, args[i]);
            }
        }
        return query;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)
     */
    public <T> Optional<T> findById(Class<T> clazz, Serializable id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (metadata == null) {
            return Optional.ofNullable(em().find(clazz, id));
        }

        LockModeType type = metadata.getLockModeType();

        return Optional.ofNullable(em().find(clazz, id, type));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#getOne(java.io.Serializable)
     */
    public <T> T getOne(Class<T> clazz, Serializable id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return em().getReference(clazz, id);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#findAll()
     */
    public <T> List<T> findAll(Class<T> clazz) {
        return getQuery(clazz, null, Sort.unsorted()).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll(ID[])
     */
    public <T> List<T> findAllById(Class<T> clazz, Iterable<Serializable> ids) {

        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        if (RepositoryUtils.entityInformation(em(), clazz).hasCompositeId()) {

            List<T> results = new ArrayList<T>();

            for (Serializable id : ids) {
                findById(clazz, id).ifPresent(results::add);
            }

            return results;
        }

        ByIdsSpecification<T> specification = new ByIdsSpecification<T>(RepositoryUtils.entityInformation(em(), clazz));
        TypedQuery<T> query = getQuery(clazz, specification, Sort.unsorted());

        return query.setParameter(specification.parameter, ids).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#findAll(org.springframework.data.domain.Sort)
     */
    public <T> List<T> findAll(Class<T> clazz, Sort sort) {
        return getQuery(clazz, null, sort).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
     */
    public <T> Page<T> findAll(Class<T> clazz, Pageable pageable) {

        if (null == pageable) {
            return new PageImpl<T>(findAll(clazz));
        }

        return findAll(clazz, (Specification<T>) null, pageable);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findOne(org.springframework.data.jpa.domain.Specification)
     */
    public <T> Optional<T> findOne(Class<T> clazz, @Nullable Specification<T> spec) {
        try {
            return Optional.of(getQuery(clazz, spec, Sort.unsorted()).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification)
     */
    public <T> List<T> findAll(Class<T> clazz, Specification<T> spec) {
        return getQuery(clazz, spec, (Sort) null).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
     */
    public <T> Page<T> findAll(Class<T> clazz, Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = getQuery(clazz, spec, pageable);
        return isUnpaged(pageable) ? new PageImpl<T>(query.getResultList())
                : readPage(query, clazz, pageable, spec);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Sort)
     */
    public <T> List<T> findAll(Class<T> clazz, Specification<T> spec, Sort sort) {
        return getQuery(clazz, spec, sort).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#count(org.springframework.data.jpa.domain.Specification)
     */
    public <T> long count(Class<T> clazz, @Nullable Specification<T> spec) {
        return executeCountQuery(getCountQuery(spec, clazz));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#save(java.lang.Object)
     */
    @Transactional
    public <T> T save(Class<T> clazz, T entity) {
        if (RepositoryUtils.entityInformation(em(), clazz).isNew(entity)) {
            em().persist(entity);
            return entity;
        } else {
            return em().merge(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#saveAndFlush(java.lang.Object)
     */
    @Transactional
    public <T> T saveAndFlush(Class<T> clazz, T entity) {
        T result = save(clazz, entity);
        flush();
        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#save(java.lang.Iterable)
     */
    @Transactional
    public <T> List<T> save(Class<T> clazz, Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<T> result = new ArrayList<T>();

        for (T entity : entities) {
            result.add(save(clazz, entity));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#flush()
     */
    @Transactional
    public void flush() {
        em().flush();
    }

    /**
     * Reads the given {@link TypedQuery} into a {@link Page} applying the given {@link Pageable} and
     * {@link Specification}.
     *
     * @param query must not be {@literal null}.
     * @param spec can be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     * @deprecated use {@link #readPage(TypedQuery, Class, Pageable, Specification)} instead
     */
    @Deprecated
    protected <T> Page<T> readPage(Class<T> clazz, TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        return readPage(query, clazz, pageable, spec);
    }

    /**
     * Reads the given {@link TypedQuery} into a {@link Page} applying the given {@link Pageable} and
     * {@link Specification}.
     *
     * @param query must not be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param spec can be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     */
    protected <T> Page<T> readPage(TypedQuery<T> query, Class<T> domainClass, Pageable pageable,
            @Nullable Specification<T> spec) {

        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(query.getResultList(), pageable,
                () -> executeCountQuery(getCountQuery(spec, domainClass)));
    }

    /**
     * Creates a new {@link TypedQuery} from the given {@link Specification}.
     *
     * @param spec can be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     */
    protected <T> TypedQuery<T> getQuery(Class<T> clazz, Specification<T> spec, Pageable pageable) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return getQuery(spec, clazz, sort);
    }

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec can be {@literal null}.
     * @param sort can be {@literal null}.
     * @return
     */
    protected <T> TypedQuery<T> getQuery(Class<T> clazz, @Nullable Specification<T> spec, Sort sort) {
        return getQuery(spec, clazz, sort);
    }

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param sort can be {@literal null}.
     * @return
     */
    protected <T> TypedQuery<T> getQuery(Specification<T> spec, Class<T> domainClass, Sort sort) {

        CriteriaBuilder builder = em().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(domainClass);

        Root<T> root = applySpecificationToCriteria(spec, domainClass, query);
        query.select(root);

        if (sort.isSorted()) {
            query.orderBy(toOrders(sort, root, builder));
        }

        return applyRepositoryMethodMetadata(em().createQuery(query));
    }

    /**
     * Creates a new count query for the given {@link Specification}.
     *
     * @param spec can be {@literal null}.
     * @return
     * @deprecated override {@link #getCountQuery(Specification, Class)} instead
     */
    @Deprecated
    protected <T> TypedQuery<Long> getCountQuery(Class<T> clazz, Specification<T> spec) {
        return getCountQuery(spec, clazz);
    }

    /**
     * Creates a new count query for the given {@link Specification}.
     *
     * @param spec can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @return
     */
    protected <T> TypedQuery<Long> getCountQuery(Specification<T> spec, Class<T> domainClass) {

        CriteriaBuilder builder = em().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = applySpecificationToCriteria(spec, domainClass, query);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        // Remove all Orders the Specifications might have applied
        query.orderBy(Collections.<Order> emptyList());

        return em().createQuery(query);
    }

    /**
     * Applies the given {@link Specification} to the given {@link CriteriaQuery}.
     *
     * @param spec can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param query must not be {@literal null}.
     * @return
     */
    private <S, U> Root<U> applySpecificationToCriteria(Specification<U> spec, Class<U> domainClass,
            CriteriaQuery<S> query) {

        Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = em().getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {
        if (metadata == null) {
            return query;
        }

        TypedQuery<S> toReturn = query.setLockMode(metadata.getLockModeType());

        return toReturn;
    }

    /**
     * Executes a count query and transparently sums up all values returned.
     *
     * @param query must not be {@literal null}.
     * @return
     */
    private static Long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");

        List<Long> totals = query.getResultList();
        Long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }

        return total;
    }

    private static boolean isUnpaged(Pageable pageable) {
        return pageable.isUnpaged();
    }

    /**
     * Specification that gives access to the {@link Parameter} instance used to bind the ids for
     * {@link SimpleJpaRepository#findAll(Iterable)}. Workaround for OpenJPA not binding collections to in-clauses
     * correctly when using by-name binding.
     *
     * @see https://issues.apache.org/jira/browse/OPENJPA-2018?focusedCommentId=13924055
     * @author Oliver Gierke
     */
    @SuppressWarnings("rawtypes")
    private static final class ByIdsSpecification<T> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;

        ParameterExpression<Iterable> parameter;

        public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
         */
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }
}