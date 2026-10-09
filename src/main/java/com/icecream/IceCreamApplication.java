package com.icecream;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.File;

@SpringBootApplication
public class IceCreamApplication {

	public static void main(String[] args) {
		createDataDirectory();
		SpringApplication.run(IceCreamApplication.class, args);
		System.out.println("🍦 冰淇淋管理系统启动成功！");
		System.out.println("📡 API 地址: http://localhost:8080/api");
	}

	/**
	 * 手动执行 Flyway 迁移（绕过自动配置）
	 */
	@Bean
	public CommandLineRunner runFlyway(DataSource dataSource) {
		return args -> {
			System.out.println("🔄 手动执行 Flyway 迁移...");
			Flyway flyway = Flyway.configure()
					.dataSource(dataSource)
					.locations("classpath:db/migration")
					.table("flyway_schema_history")
					.baselineOnMigrate(true)
					.baselineVersion("1")
					.load();
			flyway.migrate();
			System.out.println("✅ Flyway 迁移完成！");
		};
	}

	/**
	 * 检查 data 目录是否存在，不存在则创建
	 */
	private static void createDataDirectory() {
		String dataDirPath = "./data";
		File dataDir = new File(dataDirPath);

		if (!dataDir.exists()) {
			boolean created = dataDir.mkdirs();
			if (created) {
				System.out.println("📁 已创建 data 目录: " + dataDir.getAbsolutePath());
			} else {
				System.err.println("❌ 创建 data 目录失败: " + dataDir.getAbsolutePath());
			}
		} else {
			System.out.println("📁 data 目录已存在: " + dataDir.getAbsolutePath());
		}
	}
}