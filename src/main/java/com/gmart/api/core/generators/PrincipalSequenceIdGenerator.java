/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of Thales DIS.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.generators;


import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceMismatchStrategy;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 14 nov. 2020
 **/
public class PrincipalSequenceIdGenerator extends SequenceStyleGenerator {
	 public static final String VALUE_PREFIX_PARAMETER = "GMART";
	    public static final String VALUE_PREFIX_DEFAULT = "GMT";
	    private String valuePrefix;
	 
	    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
	    public static final String NUMBER_FORMAT_DEFAULT = "%d";
	    private String numberFormat;
	 
	    @Override
	    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
	        return valuePrefix + String.format(numberFormat, super.generate(session, object));
	    }
	 
	    @Override
	    public void configure(Type type, Properties params,    ServiceRegistry serviceRegistry) throws MappingException {
	        super.configure(LongType.INSTANCE, params, serviceRegistry);
	        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER,
	                params, VALUE_PREFIX_DEFAULT);
	        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER,
	                params, NUMBER_FORMAT_DEFAULT);
	    }
	 
	
}
