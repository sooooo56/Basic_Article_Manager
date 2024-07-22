package com.exam.BAM.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.exam.BAM.dto.Article;
import com.exam.BAM.dto.Member;
import com.exam.BAM.util.Util;

public class App {
	
	private int lastArticleId;
	private List<Article> articles;
	private int lastMemberId;
	private List<Member> members;
	
	public App() {
		this.lastArticleId = 0;
		this.articles = new ArrayList<>();
		this.lastMemberId = 0;
		this.members = new ArrayList<>();
	}
	
	public void run() {
		System.out.println("== 프로그램 시작 ==");
		
		makeTestData();
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.printf("명령어) ");
			
			String cmd = sc.nextLine().trim();
			
			if (cmd.equals("exit")) {
				break;
			} 
			
			if (cmd.length() == 0) {
				System.out.println("명령어를 입력해주세요");
				continue;
			}
			
			if (cmd.equals("member join")) {
				String loginId = null;
				String loginPw = null;
				String name = null;
				
				while(true) {
					System.out.printf("아이디 : ");
					loginId = sc.nextLine().trim();
					
					if (loginId.length() == 0) {
						System.out.println("아이디는 필수 입력 정보입니다");
						continue;
					}
					
					boolean isLoginIdDup = false;
					
					for (Member member : members) {
						if (loginId.equals(member.getLoginId())) {
							isLoginIdDup = true;
							break;
						}
					}
					
					if (isLoginIdDup) {
						System.out.printf("[ %s ]은(는) 이미 사용중인 아이디입니다\n", loginId);
						continue;
					}
					
					System.out.printf("[ %s ]은(는) 사용가능한 아이디입니다\n", loginId);
					break;
				}
				
				while (true) {
					System.out.printf("비밀번호 : ");
					loginPw = sc.nextLine();
					
					if (loginPw.length() == 0) {
						System.out.println("비밀번호는 필수 입력 정보입니다");
						continue;
					}
					
					System.out.printf("비밀번호 확인 : ");
					String loginPwChk = sc.nextLine();
					
					if (loginPw.equals(loginPwChk) == false) {
						System.out.println("비밀번호가 일치하지 않습니다");
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.printf("이름 : ");
					name = sc.nextLine().trim();
					
					if (name.length() == 0) {
						System.out.println("이름은 필수 입력 정보입니다");
						continue;
					}
					break;
				}
				
				lastMemberId++;
				
				Member member = new Member(lastMemberId, Util.getDateStr(), loginId, loginPw, name);
				
				members.add(member);
				
				System.out.println(name + "님이 가입되었습니다");
				
			} else if (cmd.equals("article write")) {
			
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				
				lastArticleId++;
		        
				Article article = new Article(lastArticleId, Util.getDateStr(), title, body, 0);
				
				articles.add(article);
				
				System.out.println(lastArticleId + "번 글이 생성되었습니다");
				
			} else if (cmd.startsWith("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시글이 없습니다");
					continue;
				}
				
				List<Article> printArticles = articles;
				
				String searchKeyword = cmd.substring("article list".length()).trim();
				
				if (searchKeyword.length() > 0) {
					System.out.println("검색어 : " + searchKeyword);

					printArticles = new ArrayList<>();
					
					for (Article article : articles) {
						if (article.getTitle().contains(searchKeyword)) {
							printArticles.add(article);
						}
					}
					
					if (printArticles.size() == 0) {
						System.out.println("검색결과가 없습니다");
						continue;
					}
				}
				
				System.out.println("번호	|	제목	|		작성일		|	조회수");
				
				for (int i = printArticles.size() - 1; i >= 0; i--) {
					Article article = printArticles.get(i);
					System.out.printf("%d	|	%s	|	%s	|	%d\n", article.getId(), article.getTitle(), article.getRegDate(), article.getViewCnt());
				}
				
			} else if (cmd.startsWith("article detail ")) {
				int id = getIdByCmd(cmd);
				
				if (id == 0) {
					System.out.println("명령어가 올바르지 않습니다");
					continue;
				}
				
				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.println(id + "번 게시물은 존재하지 않습니다");
					continue;
				}
				
				foundArticle.increaseViewCnt();
				
				System.out.printf("번호 : %d\n", foundArticle.getId());
				System.out.printf("작성일 : %s\n", foundArticle.getRegDate());
				System.out.printf("제목 : %s\n", foundArticle.getTitle());
				System.out.printf("내용 : %s\n", foundArticle.getBody());
				System.out.printf("조회수 : %d\n", foundArticle.getViewCnt());
				
			} else if (cmd.startsWith("article modify ")) {
				int id = getIdByCmd(cmd);
				
				if (id == 0) {
					System.out.println("명령어가 올바르지 않습니다");
					continue;
				}

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.println(id + "번 게시물은 존재하지 않습니다");
					continue;
				}

				System.out.printf("수정할 제목 : ");
				String title = sc.nextLine();
				System.out.printf("수정할 내용 : ");
				String body = sc.nextLine();

				foundArticle.setTitle(title);
				foundArticle.setBody(body);

				System.out.println(id + "번 게시물을 수정했습니다");

			} else if (cmd.startsWith("article delete ")) {
				
				int id = getIdByCmd(cmd);
				
				if (id == 0) {
					System.out.println("명령어가 올바르지 않습니다");
					continue;
				}
				
				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.println(id + "번 게시물은 존재하지 않습니다");
					continue;
				}
				
				articles.remove(foundArticle);
				
				System.out.println(id + "번 게시물을 삭제했습니다");
				
			} else {
				System.out.println("존재하지 않는 명령어 입니다");
			}
		}
		
		sc.close();
		
		System.out.println("== 프로그램 끝 ==");
	}
	
	private int getIdByCmd(String cmd) {
		String[] cmdBits = cmd.split(" ");
		
		try {
			int id = Integer.parseInt(cmdBits[2]);
			return id;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private Article getArticleById(int id) {
		for (Article article : articles){
			if (id == article.getId()) {
				return article;
			}
		}
		return null;
	}
	
	private void makeTestData() {
		System.out.println("테스트용 게시물 데이터 3개를 생성했습니다");
		System.out.println("테스트용 회원 데이터 3개를 생성했습니다");
		for (int i = 1; i <= 3; i++) {
			articles.add(new Article(++lastArticleId, Util.getDateStr(), "제목" + i, "내용" + i, i * 10));
			members.add(new Member(++lastMemberId, Util.getDateStr(), "test" + i, "test" + i, "유저" + i));
		}
	}
}