package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.models.DBConnection;
import com.models.UserModel;

@Path("/")
public class Services {

	/*
	 * @GET
	 * 
	 * @Path("/signup")
	 * 
	 * @Produces(MediaType.TEXT_HTML) public Response signUp(){ return
	 * Response.ok(new Viewable("/Signup.jsp")).build(); }
	 */

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,
			@FormParam("email") String email, @FormParam("pass") String pass) {
		UserModel user = UserModel.addNewUser(name, email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email,
			@FormParam("pass") String pass) {
		UserModel user = UserModel.login(email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}
	
	@POST
	@Path("/updatePosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@FormParam("id") String id,
			@FormParam("lat") String lat, @FormParam("long") String lon) {
		Boolean status = UserModel.updateUserPosition(Integer.parseInt(id), Double.parseDouble(lat), Double.parseDouble(lon));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	
	@POST
	@Path("/userLastPosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String userLastPosition(@FormParam("id") String id) throws NumberFormatException, SQLException {
		UserModel user = UserModel.LastPosition(Integer.parseInt(id));
		JSONObject json = new JSONObject();
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}
	@POST
	@Path("/followUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String followUser(@FormParam("email1") String email1, @FormParam("email2") String email2) throws SQLException {
		Boolean status = UserModel.follow(email1, email2);
		JSONObject json = new JSONObject();
		json.put("status",  status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/unfollowUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String unfollowUser(@FormParam("email1") String email1, @FormParam("email2") String email2) throws SQLException {
		Boolean status = UserModel.unfollow(email1, email2);
		JSONObject json = new JSONObject();
		json.put("status",  status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/getFollowers")
	@Produces(MediaType.TEXT_PLAIN)
	//public String getFollowers(@FormParam("email") String email) throws SQLException
	public String getFollowers(@FormParam("id") String id) throws SQLException
	{
		ArrayList<Integer> user = UserModel.followers(Integer.parseInt(id));
		JSONObject json = new JSONObject();
		json.put("followers", user);
		return json.toJSONString();
	}
	
	@POST
	@Path("/addPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String addPlaces(@FormParam("name") String name, @FormParam("description") String description,
	@FormParam("lat") String lat, @FormParam("long") String lon) throws NumberFormatException, SQLException
		{
			Boolean status = UserModel.addNewPlace(name, description, Double.parseDouble(lat), Double.parseDouble(lon));
			JSONObject json = new JSONObject();
			json.put("status", status ? 1 : 0);
			return json.toJSONString();
		}
	
	@POST
	@Path("/savePlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String savePlaces (@FormParam ("email")String email , @FormParam ("placeName") String placeName){
		
		boolean status = UserModel.savePlace(email , placeName);
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/checkin")
	@Produces (MediaType.TEXT_PLAIN)
	public String checkin (@FormParam ("email") String email , @FormParam ("placeName") String placeName){
		
		boolean status = UserModel.checkIn(email , placeName);
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	
	@POST
	@Path("/likeCheckin")
	@Produces (MediaType.TEXT_PLAIN)
	public String likeCheckin (@FormParam ("email") String email , @FormParam ("checkinid") String checkinID){
		
		boolean status = UserModel.like(email , Integer.parseInt(checkinID));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/commentCheckin")
	@Produces (MediaType.TEXT_PLAIN)
	public String commetnOnCheckin (@FormParam ("email") String email , @FormParam ("checkinid") String checkinID , 
			@FormParam ("desc") String desc){
		
		boolean status = UserModel.comment(email , Integer.parseInt(checkinID) , desc);
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getJson() {
		return "Hello after editing";
		// Connection URL:
		// mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/
	}
}
