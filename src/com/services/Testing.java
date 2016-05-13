package com.services;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Testing {
  @Test
  public void follow() throws SQLException {
	  Services s = new Services();
	  String result = s.followUser("m.samir", "ahmedelhennawy");
	  Assert.assertEquals("{" + "\"status\""+":"+0 + "}", result);
  }
  
  @Test
  public void unFollow() throws SQLException {
	  Services s = new Services();
	  String result = s.unfollowUser("m.samir", "ahmedelhennawy");
	  Assert.assertEquals("{" + "\"status\""+":"+1 + "}", result);
  }
  
  @Test
  public void getFollowers() throws SQLException{
	  Services s = new Services();
	  String result = s.getFollowers("6");
	  Assert.assertEquals("{" + "\"followers\""+":"+ "["+1+","+7+","+8+"]"+ "}", result);
  }
}

