package net.gamebacon.luckywilliams.login_user;

public enum Gender {
    NA("Not available"),
    MALE("Male"),
    FEMALE("Female"),
    APACHE_HELICOPTER("Apache Helicopter");


    private String name;

    Gender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


