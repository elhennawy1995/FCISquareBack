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
	
	/**
	 * 
	 * @param userEmail
	 * @return userID
	 * @throws SQLException
	 */
	static int getuserID (String userEmail) throws SQLException{
		
		Connection conn = DBConnection.getActiveConnection();
		String sql = ("Select `id` from users where `email` = '"+userEmail+"'");
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int userID = 0;
		if(rs.next()){
			userID = rs.getInt("id");
		}
		return userID;
	}
	
	/**
	 * 
	 * @param name
	 * @param email
	 * @param pass
	 * @return user
	 */
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
	
	/**
	 * 
	 * @param email
	 * @param pass
	 * @return user
	 */
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
	
	/**
	 * 
	 * @param id
	 * @param lat
	 * @param lon
	 * @return true
	 */
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
	
	/**
	 * 
	 * @param id
	 * @return user
	 * @throws SQLException
	 */
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
	
	/**
	 * 
	 * @param userEmail
	 * @param followingUserEmail
	 * @return true or false
	 * @throws SQLException
	 */
	public static Boolean follow(String userEmail, String followingUserEmail) throws SQLException {
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;

			int userID = getuserID(userEmail);
			int followingID = getuserID(followingUserEmail);

			String sql3 = ("Insert into followers (`id1`, `id2`) VALUES  ('"+userID+"','"+followingID+"')");
			stmt = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			return true;		
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param userEmail
	 * @param followingUserEmail
	 * @return true or false
	 */
	public static Boolean unfollow(String userEmail, String followingUserEmail)
	{
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;

			int userID = getuserID(userEmail);
			int followingID = getuserID(followingUserEmail);

			String sql3 = ("Delete from followers where `id1` = '"+userID+"' and `id2` = '"+followingID+"'");
			stmt = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			return true;		
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @return list of following
	 * @throws SQLException
	 */
	public static ArrayList<Integer> following(int id) throws SQLException
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
	
	/**
	 * 
	 * @param placeName
	 * @param description
	 * @param lat
	 * @param lon
	 * @return true or false
	 * @throws SQLException
	 */
	public static Boolean addNewPlace(String placeName, String description, Double lat, Double lon) throws SQLException
	{
		try 
		{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into places (`name`,`description`,`lat`, `long`) VALUES  (?,?,?,?)";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, placeName);
			stmt.setString(2, description);
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @param placeName
	 * @return true or false
	 */
	public static Boolean savePlace (String userEmail , String placeName){
		try{

			Connection conn = DBConnection.getActiveConnection();
			int userID = getuserID(userEmail);
			
			String sql2 = ("Select `id` from places where `name` = '"+placeName+"'");
			PreparedStatement stmt2;
			stmt2 = conn.prepareStatement(sql2);
			ResultSet rs2 = stmt2.executeQuery();
			int placeID =0;
			if(rs2.next()){
				placeID = rs2.getInt("id");
			}
			
			String sql3 = ("insert into savedplaces(`user_ID` , `place_ID`) values (? ,?)");
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt3.setInt(1, userID);
			stmt3.setInt(2, placeID);
			stmt3.executeUpdate();
			
			return true ;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
				
		return false;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @param placeName
	 * @return true or false
	 */
	public static Boolean checkIn (String userEmail , String placeName ){
		try{
			Connection conn = DBConnection.getActiveConnection();

			int userID = getuserID(userEmail);
			
			String sql2 = ("Select `id` from places where `name` = '"+placeName+"'");
			PreparedStatement stmt2;
			stmt2 = conn.prepareStatement(sql2);
			ResultSet rs2 = stmt2.executeQuery();
			int placeID =0;
			if(rs2.next()){
				placeID = rs2.getInt("id");
			}
			
			String sql3 = ("insert into checkin(`userID` , `placeID`) values ('"+userID+"' ,'"+placeID+"')");
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt3.executeUpdate();
			
			return true ;
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @param checkinID
	 * @return true or false
	 */
	public static Boolean like (String userEmail , int checkinID){
		
		try{
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;

			int userID = getuserID(userEmail);
			
			String sql3 = ("insert into likes(`userID` , `checkinID`) values ('"+userID+"' ,'"+checkinID+"')");
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt3.executeUpdate();
			
			UserModel m = new UserModel ();
			m.notify(userID, checkinID, "Like");
			return true;
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @param checkinID
	 * @param description
	 * @return true or false
	 */
	public static Boolean comment (String userEmail , int checkinID , String description){
		try{
			Connection conn = DBConnection.getActiveConnection();
			
			int userID = getuserID(userEmail);
			
			String sql3 = ("insert into comment(`user-ID` , `checkin-ID` , `description`) values ('"+userID+"' ,'"+checkinID+"' , '"+description+"')");
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
			stmt3.executeUpdate();
			
			UserModel m = new UserModel ();
			m.notify(userID, checkinID, "comment");
			
			return true;
		}
			
		catch(SQLException e){
			e.printStackTrace();
		}
		return false ;
	}
	
	/**
	 * 
	 * @param userID
	 * @param checkinID
	 * @param type
	 * @return true or false
	 */
	public  Boolean notify (int userID , int checkinID , String type){
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = ("insert into notification(`userID` , `checkinID` , `type`) values ('"+userID+"' ,'"+checkinID+"' , '"+type+"')");
			PreparedStatement stmt3;
			stmt3 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt3.executeUpdate();
			
			return true;
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return false ;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @return list of notifications
	 * @throws SQLException
	 */
	public static ArrayList<Integer> getAllNotification(String userEmail)throws SQLException {

		Connection conn = DBConnection.getActiveConnection();

		int userID = getuserID(userEmail);

		ArrayList<Integer> checkInIds = new ArrayList<Integer>();

		String sql1 = ("select `checkinID` from checkin where `userID` ='"+ userID + "' ");
		PreparedStatement stmt1;
		stmt1 = conn.prepareStatement(sql1);
		ResultSet rs1 = stmt1.executeQuery();
		int cid = 0;
		while (rs1.next()) {
			checkInIds.add(rs1.getInt("checkinID"));
		}
		
		return checkInIds;
	}
	
}
