package managesys.configuration;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datasource.hibernate")
public class HibernateProperties {

    private String dialect;
    private String ddlAuto;
    private boolean showSql;
    private boolean formatSql;
    private boolean nonContextualCreation = false;

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", formatSql);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", nonContextualCreation);
        return properties;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public boolean isFormatSql() {
        return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    public boolean isNonContextualCreation() {
        return nonContextualCreation;
    }

    public void setNonContextualCreation(boolean nonContextualCreation) {
        this.nonContextualCreation = nonContextualCreation;
    }
}
