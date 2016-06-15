package me.yczhang.kit.udp_server;

import io.netty.channel.socket.DatagramPacket;

/**
 * Created by YCZhang on 1/7/16.
 */
public interface UdpAction {

	public void action(DatagramPacket packet);
}
