/**
 * 
 */
package org.yinyayun.ai.baidu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yinyayun.ai.utils.TxtFileReader;

/**
 * @author yinyayun
 *
 */
public class BaiduSentenceTransformer {
	public static void main(String[] args) throws Exception {
		try (TxtFileReader txtFileReader = new TxtFileReader("data/sentence.txt")) {
			while (txtFileReader.hasNext()) {
				String line = txtFileReader.readLine();
				Item[] items = parserToItems(line);
				ItemNode node = buildTree(items);
				System.out.println(node);
			}
		}
	}

	private static ItemNode buildTree(Item[] items) {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < items.length; i++) {
			int currentId = i + 1;
			int parent = items[i].head;
			map.putIfAbsent(parent, new ArrayList<Integer>());
			map.get(parent).add(currentId);
		}
		int parent = map.get(0).get(0);
		ItemNode root = new ItemNode(items[parent - 1]);
		buildTree(items, map, root);
		return root;
	}

	private static void buildTree(Item[] items, Map<Integer, List<Integer>> map, ItemNode root) {
		List<Integer> ids = map.get(root.nodeId());
		if (ids == null) {
			return;
		}
		for (Integer id : ids) {
			ItemNode node = new ItemNode(items[id - 1]);
			root.addChild(node);
			buildTree(items, map, node);
		}
	}

	private static Item[] parserToItems(String line) {
		JSONObject json = new JSONObject(line);
		JSONArray arrays = json.getJSONArray("items");
		Item[] items = new Item[arrays.length()];
		for (int i = 0; i < items.length; i++) {
			JSONObject jsonObject = arrays.getJSONObject(i);
			String word = jsonObject.getString("word");
			String postag = jsonObject.getString("postag");
			String deprel = jsonObject.getString("deprel");
			int head = jsonObject.getInt("head");
			items[i] = new Item(i + 1, word, postag, deprel, head);
		}
		return items;
	}

	static class ItemNode {
		Item item;
		List<ItemNode> leftChilds;
		List<ItemNode> rightChilds;
		Map<Integer, ItemNode> nodesMap = new HashMap<Integer, ItemNode>();

		public ItemNode(Item item) {
			this.item = item;
		}

		public ItemNode seekItemNode(int id) {
			ItemNode node = nodesMap.get(id);
			if (node != null) {
				return node;
			}
			for (ItemNode itemNodenode : nodesMap.values()) {
				ItemNode seek = itemNodenode.seekItemNode(id);
				if (seek != null) {
					return seek;
				}
			}
			return null;
		}

		public int nodeId() {
			return item.currentId;
		}

		public void addChild(ItemNode node) {
			if (node.nodeId() < nodeId()) {
				if (leftChilds == null) {
					leftChilds = new ArrayList<ItemNode>();
				}
				leftChilds.add(node);
			} else {
				if (rightChilds == null) {
					rightChilds = new ArrayList<ItemNode>();
				}
				rightChilds.add(node);
			}
			nodesMap.put(node.nodeId(), node);
		}

		@Override
		public String toString() {
			return "ItemNode [item=" + item + ", leftChilds=" + leftChilds + ", rightChilds=" + rightChilds + "]";
		}

	}

	static class Item {
		int currentId;
		String word;
		String pos;
		String deprel;
		int head;

		public Item(int currentId, String word, String pos, String deprel, int head) {
			super();
			this.currentId = currentId;
			this.word = word;
			this.pos = pos;
			this.deprel = deprel;
			this.head = head;
		}

		@Override
		public String toString() {
			return "Item [word=" + word + ", pos=" + pos + ", deprel=" + deprel + ", head=" + head + "]";
		}
	}
}
