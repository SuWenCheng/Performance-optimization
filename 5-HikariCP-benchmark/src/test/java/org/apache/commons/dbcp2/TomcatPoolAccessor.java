package org.apache.commons.dbcp2;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

public final class TomcatPoolAccessor extends DataSource
{
   public TomcatPoolAccessor(PoolProperties props)
   {
      super(props);

      try {
         // forces internal pool creation
         super.createPool();
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
   }

   @Override
   public Connection getConnection() throws SQLException
   {
      return super.getConnection();
   }

   public int getNumTotal()
   {
      return pool.getActive() + pool.getIdle();
   }
}
