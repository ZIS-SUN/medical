import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        // 生成admin123的密码哈希
        String password = "admin123";
        String hash = encoder.encode(password);

        System.out.println("密码: " + password);
        System.out.println("BCrypt哈希: " + hash);

        // 验证
        System.out.println("验证: " + encoder.matches(password, hash));

        // 生成123456的密码哈希
        String password2 = "123456";
        String hash2 = encoder.encode(password2);

        System.out.println("\n密码: " + password2);
        System.out.println("BCrypt哈希: " + hash2);
        System.out.println("验证: " + encoder.matches(password2, hash2));
    }
}
