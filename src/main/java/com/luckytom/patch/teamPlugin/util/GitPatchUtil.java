package com.luckytom.patch.teamPlugin.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import com.luckytom.patch.util.DateUtil;

public class GitPatchUtil {

	/**
	 * 从git获取补丁文件列表
	 * 
	 * @return
	 */
	public static List<String> getPatchList() {
		String uri = "D:/代码/学习/patch/.git";
		try (Git git = Git.open(new File(uri));){
			Iterable<RevCommit> logs = git.log().call();
			if (null != logs) {
				for(RevCommit revCommit:logs){
					System.out.println("id==>"+revCommit.name());
					System.out.println("msg==>"+revCommit.getFullMessage());
					PersonIdent committerIdent = revCommit.getCommitterIdent();
					System.out.println("author==>"+committerIdent.getName());
					System.out.println("time==>"+DateUtil.convertDateToStr(committerIdent.getWhen()));
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		getPatchList();
	}

}
