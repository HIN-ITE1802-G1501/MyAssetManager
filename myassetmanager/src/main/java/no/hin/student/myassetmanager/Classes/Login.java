package no.hin.student.myassetmanager.Classes;


public class Login {
    public enum UserRole {
        ADMIN_USER(1),
        REGULAR_USER(2),
        LOGGED_OUT(9);

        private int userRoleId;

        private UserRole(int id) {
            userRoleId = id;
        }
    }

    private static User loginUser;

    private static String loginUsername = "";
    private static String loginPassword = "";
    private static Boolean loginAdmin = false;

    private static UserRole loginUserRole = UserRole.LOGGED_OUT;

    public static void setUsername(String username) {
        loginUsername = username;
    }

    public static String getUsername() {
        return loginUsername;
    }

    public static void setPassword(String password) {
        loginPassword = password;
    }

    public static String getPassword() {
        return loginPassword;
    }

    public static void setAdmin(Boolean admin) {
        loginAdmin = admin;
    }

    public static Boolean getAdmin() {
        return loginAdmin;
    }

    public static void setUserRole(UserRole userRole) {
        loginUserRole = userRole;
    }

    public static UserRole getUserRole() {
        return loginUserRole;
    }

    public static Boolean isLoggedOut() {
        return loginUserRole == UserRole.LOGGED_OUT ? true : false;
    }

    public static Boolean isLoggedInAsAdminUser() {
        return loginUserRole == UserRole.ADMIN_USER ? true : false;
    }

    public static Boolean isLoggedInAsRegularUser() {
        return loginUserRole == UserRole.REGULAR_USER ? true : false;
    }

    public static User getLoggedInUser() {
        return loginUser;
    }

    public static void setLoggedInUser(User user) {
        loginUser = user;
    }
}
