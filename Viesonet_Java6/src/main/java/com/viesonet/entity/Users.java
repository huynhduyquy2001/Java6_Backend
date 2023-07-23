package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class Users {
	@Id
	private String userId;
	private String username;
	private String address;
	private String relationship;
	private String introduction;
	private boolean gender;
	private Date birthday;
	private String avatar;
	private String background;
	private int violationCount;
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessTime;
	
	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private Accounts account;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Posts> posts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "responder")
	private List<Reply> replyResponder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<Reply> replyReceiver;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Favorites> favorites;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Comments> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<Notifications> notifications;
	
	@JsonIgnore
	@OneToMany(mappedBy = "follower")
	private List<Follow> followers;
	
	@JsonIgnore
	@OneToMany(mappedBy = "following")
	private List<Follow> followings;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Violations> violations;

}