package org.fastds.service;

import java.io.IOException;

import org.junit.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerService {
	private DockerClient docker;
	private long Mem = 1024 * 1024 * 1024 * 1;

	public DockerService() {
		docker = DockerClientBuilder.getInstance("http://localhost:2735")
				.build();
	}

	public boolean isRunning(String name) {
		InspectContainerResponse inspectContainerResponse = docker
				.inspectContainerCmd(name).exec();
		return inspectContainerResponse.getState().isRunning();
	}

	// start container with SciDB running
	public void startContainer(String name) {
		docker.startContainerCmd("/scidb.sh").withContainerId(name).exec();
		try {
			docker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startCluster(String name) {

	}

	public void restartContainer(String name) {
		docker.restartContainerCmd("/scidb.sh").withContainerId(name);
		try {
			docker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIp(String name) {

		if (!isRunning(name)) {

			startContainer(name);
		}

		docker = DockerClientBuilder.getInstance("http://localhost:2735")
				.build();
		InspectContainerResponse inspectContainerResponse = docker
				.inspectContainerCmd(name).exec();
		String ip = inspectContainerResponse.getNetworkSettings()
				.getIpAddress();
		try {
			docker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public void createCluster(String name) {

	}

	public void createWorker(String name, int num) {

		for (int i = 2; i <= num; i++) {
			CreateContainerResponse container = docker
					.createContainerCmd("scidbworker").withName(name + i)
					.withMemoryLimit(Mem).exec();
			docker.startContainerCmd(container.getId()).exec();
		}

		try {
			docker.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	@Test
	public void test(){
		createContainerWithName("zhouyu123");
	}
	public void createContainerWithName(String name) {
		Volume volume1 = new Volume("/var/scidb/data");
		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp1239 = ExposedPort.tcp(1239);
		CreateContainerResponse container = docker
				.createContainerCmd("scidb:2.0").withCmd("/scidb.sh")
				.withExposedPorts(tcp22, tcp1239).withName(name)
				.withMemoryLimit(Mem)
				.withBinds(new Bind("/home/123/Desktop/scidb", volume1)).exec();
		docker.startContainerCmd(container.getId()).exec();
		InspectContainerResponse inspectContainerResponse = docker
				.inspectContainerCmd(container.getId()).exec();
		System.out.println(inspectContainerResponse.getState().isRunning());
		// container.
		try {
			docker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
