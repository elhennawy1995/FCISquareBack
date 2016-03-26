package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class UserModel {

	
	private String name;
	private String email;
	private String pass;
	private Integer id;
	private Double lat;
	private Double lon;
	
	public String getPass(){
		return pass;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public static UserModel addNewUser(String name, String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into users (`name`,`email`,`password`) VALUES  (?,?,?)";
			// System.out.println(sql);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = email;
				user.pass = pass;
				user.name = name;
				user.lat = 0.0;
				user.lon = 0.0;
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static UserModel login(String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `email` = ? and `password` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = rs.getString("email");
				user.pass = rs.getString("password");
				user.name = rs.getString("name");
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean updateUserPosition(Integer id, Double lat, Double lon) {
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Update users set `lat` = ? , `long` = ? where `id` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static UserModel LastPosition(Integer id) throws SQLException {
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select `lat`, `long` from users where `id` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
				return user;
			}
			return null;
		}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return null;	
	}
	
	public static Boolean follow(String email1, String email2) throws SQLException {
		try {
			Connection conn = DBConnection.getActiveConnection();
			
			String sql1 = ("Select `id` from users where `email` = ?");
			String sql2 = ("Select `id` from users where `email` = ?");
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql1);
			stmt.setString(1, email1);
			ResultSet rs1 = stmt.executeQuery();
			UserModel user1 = new UserModel();
			UserModel user2 = new UserModel();
			if(rs1.next())
			{
				user1.id = rs1.getInt("id");
			}
			stmt = conn.prepareStatement(sql2);
			stmt.setString(1, email2);
			ResultSet rs2 = stmt.executeQuery();
			if(rs2.next())
			{
				user2.id = rs2.getInt("id");
			}
			String sql3 = ("Insert into followers (`id1`, `id2`) VALUES  (?,?)");
			stmt = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, user1.id);
			stmt.setInt(2, user2.id);
			stmt.executeUpdate();
			return true;		
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	public static Boolean unfollow(String email1, String email2)
	{
		try {
			Connection conn = DBConnection.getActiveConnection();
			
			String sql1 = ("Select `id` from users where `email` = ?");
			String sql2 = ("Select `id` from users where `email` = ?");
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql1);
			stmt.setString(1, email1);
			ResultSet rs1 = stmt.executeQuery();
			UserModel user1 = new UserModel();
			UserModel user2 = new UserModel();
			if(rs1.next())
			{
				user1.id = rs1.getInt("id");
			}
			stmt = conn.prepareStatement(sql2);
			stmt.setString(1, email2);
			ResultSet rs2 = stmt.executeQuery();
			if(rs2.next())
			{
				user2.id = rs2.getInt("id");
			}
			String sql3 = ("Delete from followers where `id1` = ? and `id2` = ?");
			stmt = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, user1.id);
			stmt.setInt(2, user2.id);
			stmt.executeUpdate();
			return true;		
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Integer> followers(int id) throws SQLException
	{
		Connection conn = DBConnection.getActiveConnection();
		String sql1 = ("Select `id2` from followers where `id1` = ?");
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sql1);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		ArrayList<Integer> ids = new ArrayList <Integer>();
		ArrayList<String> names = new ArrayList <String>();
		while (rs.next()){
			ids.add(rs.getInt("id2"));
		}
		return ids;
	/*	int i=0;
		String sql2 = ("select `name` from users where `id`=?");
		PreparedStatement stmt1;
		stmt1 = conn.prepareStatement(sql2);
		stmt1.setInt(1 , ids.get(i));
		ResultSet rs1 = stmt1.executeQuery();
		i++;
	/*	if (rs1.next()){
			names.add(rs1.getString("name"));
		}
		
		
		int size = ids.size();
		
		while(rs1.next() && i<size){
			sql2 = ("select `name` from users where `id`=?");
			
			stmt1 = conn.prepareStatement(sql2);
			stmt1.setInt(1 , ids.get(i));
			rs1 = stmt1.executeQuery();
			rs1 = stmt1.executeQuery();
			names.add(rs1.getString("name"));
			//stmt1 = conn.prepareStatement(sql2);
			//stmt1.setInt(1 , ids.get(i));
			i++;
		}
	//	stmt1.setInt(1 , ids.get(i));
	//	ResultSet rs1 = stmt1.executeQuery();
	//	i=1;
	//	int s=0;
	//	while (rs1.next() && i <size){
	//		stmt1.setInt(1 , ids.get(i));
		//	rs1=stmt1.executeQuery();
		//	names.add(rs1.getString("name"));
		//	i++;
		//}
		return names;
		*/
		
	}

}
