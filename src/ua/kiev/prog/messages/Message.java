package ua.kiev.prog.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.kiev.prog.users.User;
import ua.kiev.prog.utils.Utils;
import ua.kiev.prog.chatroom.Chatroom;
import ua.kiev.prog.users.UserStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Message {
	private Date date = new Date();
	private String from;
	private String to;
	private String text;

	public Message(String from, String text) {
		this.from = from;
		this.text = text;
		this.to = "all";
	}

	public Message(String from, String text, String to) {
		this.from = from;
		this.text = text;
		this.to = to;
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public static Message fromJSON(String s) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, Message.class);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("[").append(date)
				.append(", From: ").append(from).append(", To: ").append(to)
				.append("] ").append(text)
                .toString();
	}

	public static void message(User user, Scanner scanner) {
		try {
			Thread th = new Thread(new GetThread(user));
			th.setDaemon(true);
			th.start();
			Message m;

			System.out.println("Enter your message: ");
			while (true) {
				try {
					String text = scanner.nextLine();
					if (text.isEmpty()) {
						th.stop();
						return;
					} else if (text.contains("@")) {
						String to = text.substring(text.indexOf('@') + 1, text.indexOf(' ')).strip();
						if (to.isBlank()) {
							System.out.println("User name or Chatroom name is incorrect. Message didn't send. Try again...");
							continue;
						} else {
							List<UserStatus> list = UserStatus.request();
							if (list == null ||
									(list.stream().noneMatch(us -> us.getLogin().equals(to)) &&
									!Chatroom.getMemberChatroom(to, user.getLogin()))) {
								System.out.println("User or Chatroom does not exist. Message didn't send. Try again...");
								continue;
							}
						}
						text = text.substring(text.indexOf(' ')).strip();
						m = new Message(user.getLogin(), text, to);
					} else {
						m = new Message(user.getLogin(), text);
					}
					int res = m.send(Utils.getURL() + "/add");

					if (res != 200) { // 200 OK
						System.out.println("HTTP error occurred: " + res);
						return;
					}
				} catch (StringIndexOutOfBoundsException boundsException) {
					System.out.println("User name or Chatroom name is incorrect. Message didn't send. Try again...");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private int send(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
	
		OutputStream os = conn.getOutputStream();
		try {
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode();
		} finally {
			os.close();
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
