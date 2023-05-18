package com.example.app1.ConnectActivity;

public class User{
        private String name;
        private String password;

        public User(){};
        public User (String name, String password){
                this.name =name;
                this.password =password;
        }

        public String getName(){
                return name;
        }
        public void setName (String newname) {
                name = newname;
        }
        public String getPassword(){
                return password;
        }

        public void setPassword(String newpassword) {
                password = newpassword;
        }
}