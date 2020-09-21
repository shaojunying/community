package com.shao.community.util;

import org.apache.commons.lang3.CharUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: shao
 * Date: 2020-09-21
 * Time: 17:26
 */
@Component
public class TrieTreeUtil {

    // Trie树的根节点
    private final TrieNode root = new TrieNode();
    private final String replacement = "***";
    @Value("${community.file.sensitive-words}")
    private String sensitiveWordsFileName;

    @PostConstruct
    public void init() {
        // 从敏感词文件中读取敏感词,并存入Trie树中
        try (
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sensitiveWordsFileName);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
            String keyword;
            while ((keyword = bufferedReader.readLine()) != null) {
                addKeyword(keyword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addKeyword(String keyword) {
        // 将keyword存入Trie树中
        TrieNode node = root;
        char[] chars = keyword.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // 子节点中没有该节点
            TrieNode newNode = node.getTrieNode(chars[i]);
            if (newNode == null) {
                newNode = new TrieNode();
                node.addTrieNode(chars[i], newNode);
            }
            // 修改敏感词最后一位的标志位
            if (i == chars.length - 1) {
                newNode.setKeyWordEnd(true);
            }
            node = newNode;
        }
    }


    public String filter(String originalString) {
        // 将原始字符串中的敏感词替换掉
        // Trie树中查询到的节点
        TrieNode cur = root;
        // [start, end)当前匹配成功的字符串
        int start = 0, end = 0;
        char[] chars = originalString.toCharArray();
        StringBuilder ans = new StringBuilder();
        while (end < chars.length) {
            if (isSymbol(chars[end])) {
                if (cur == root) {
                    // 根节点
                    ans.append(chars[start]);
                    start++;
                    end = start;
                } else {
                    end++;
                }
            } else {
                TrieNode subNode = cur.getTrieNode(chars[end]);
                if (subNode == null) {
                    // [start, end)没有匹配上, 将从start+1再开始匹配
                    ans.append(chars[start]);
                    start++;
                    end = start;
                    cur = root;
                } else {
                    // 匹配成功
                    if (subNode.isKeyWordEnd()) {
                        // 匹配到一个完整的敏感词 ==> 替换掉[start,end)之间的字符
                        ans.append(replacement);
                        start = end + 1;
                        end = start;
                        cur = root;
                    } else {
                        // 还要继续向后匹配
                        cur = subNode;
                        end++;
                    }
                }
            }
        }
        // 将[start, end)添加进去
        ans.append(chars, start, chars.length - start);
        return ans.toString();
    }

    private boolean isSymbol(char c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private static class TrieNode {
        // key ==> 下一级的值 value ==> 节点
        private final Map<Character, TrieNode> map = new HashMap<>();
        // 关键词末尾标识
        private boolean isKeyWordEnd;

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        public void addTrieNode(Character character, TrieNode trieNode) {
            map.put(character, trieNode);
        }

        public TrieNode getTrieNode(Character character) {
            return map.get(character);
        }
    }
}
