package org.flanzone;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.flanzone.entity.Student;
import org.flanzone.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class ShardingJdbcApplicationTests {
	@Autowired
	private StudentMapper studentMapper;

	@Test
	void selectTest() {
		studentMapper.selectList(null).forEach(System.out::println);
	}

	@Test
	void insertTest() {
		studentMapper.insert(new Student(null,"FlanChan","女"));
	}


	@Test
	void conditionalSelect() {
		LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeLeft(Student::getSname,"ccc");
		studentMapper.selectList(queryWrapper).forEach(System.out::println);
	}

}
