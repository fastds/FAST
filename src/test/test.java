package test;

import java.io.IOException;
import java.sql.Connection;

import org.fastds.dbutil.DBConnection;
import org.junit.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

public class test {
	private long Mem = 1024 * 1024 * 1024 * 1;

	@Test
	public void create() throws IOException {
		Volume volume1 = new Volume("/var/scidb/data");
		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(1239);
		Ports portBindings = new Ports();
		portBindings.bind(tcp23, Ports.Binding(49903));
		DockerClient docker = DockerClientBuilder.getInstance(
				"http://localhost:2735").build();
		CreateContainerResponse container = docker
				.createContainerCmd("scidb:2.0").withCmd("/scidb.sh")
				.withExposedPorts(tcp22, tcp23).withName("fast")
				.withBinds(new Bind("/home/scidb/Desktop/dockerdata", volume1))
				.withMemoryLimit(Mem)
				.withPortBindings(portBindings).exec();
		docker.startContainerCmd(container.getId()).exec();
		docker.close();
	}
	@Test
	public void db() throws IOException {
		Connection con = DBConnection.getConnection("192.168.221.131");
		System.out.println(con);
	}
}
