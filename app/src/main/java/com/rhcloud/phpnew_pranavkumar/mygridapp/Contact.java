package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 6/27/2015.
 */
public class Contact {

    //private variables


    String _name;

    // Empty constructor
    public Contact(){

    }

    // constructor
    public Contact( String _name){
        this._name = _name;

    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }


}
