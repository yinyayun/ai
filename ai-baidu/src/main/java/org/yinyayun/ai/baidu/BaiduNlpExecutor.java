package org.yinyayun.ai.baidu;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.yinyayun.ai.baidu.api.BaiduApi;
import org.yinyayun.ai.baidu.task.TextEntity;
import org.yinyayun.ai.baidu.utils.AppConfig;
import org.yinyayun.ai.baidu.utils.PropertiesUtil;
import org.yinyayun.ai.utils.TxtFileReader;
import org.yinyayun.ai.utils.proxy.ProxyFactory;

public abstract class BaiduNlpExecutor {

	public void executor(String dataFile, String saveFile, String completeFile, int threadSize, ProxyFactory factory) {
		BlockingQueue<TextEntity> queue = new ArrayBlockingQueue<TextEntity>(1000);
		BlockingQueue<TextEntity> saveQueue = new ArrayBlockingQueue<TextEntity>(1000);
		TxtFileReader dataReader = null;
		try {
			// 加载已经完成
			Set<Integer> completes = loadCompletes(completeFile);
			// 打开资源
			dataReader = new TxtFileReader(dataFile);
			// 启动跑数据线程
			ExecutorService executorService = startCrawlerThread(queue, saveQueue, threadSize, factory);
			// 启动保存数据线程
			startSaveThread(saveQueue, new File(saveFile), new File(completeFile));
			int id = 0;
			while (dataReader.hasNext()) {
				TextEntity entity = new TextEntity(++id, dataReader.readLine());
				if (!completes.contains(entity.id)) {
					queue.put(entity);
				}
				if (id % 1000 == 0) {
					System.out.println("read complete " + id);
				}
			}
			System.out.println("读结束...");
			while (queue.size() > 0) {
				Thread.sleep(5000);
			}
			Thread.sleep(5000);
			executorService.shutdown();
			System.out.println("程序退出...");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(dataReader);
		}
	}

	/**
	 * 启动保存线程
	 * 
	 * @param saveQueue
	 * @param saveFile
	 * @param completeSaveFile
	 */
	public void startSaveThread(BlockingQueue<TextEntity> saveQueue, File saveFile, File completeSaveFile) {
		Runnable run = () -> {
			while (true) {
				try {
					TextEntity entity = saveQueue.take();
					String json = entity.response;
					int id = entity.id;
					FileUtils.write(saveFile, json.concat("\n"), "utf-8", true);
					FileUtils.write(completeSaveFile, String.valueOf(id).concat("\n"), "utf-8", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(run).start();
	}

	/**
	 * 启动抓取的执行线程
	 * 
	 * @param queue
	 * @param saveQueue
	 * @param threadSize
	 * @param apiCreateFunction
	 * @return
	 */
	public ExecutorService startCrawlerThread(BlockingQueue<TextEntity> queue, BlockingQueue<TextEntity> saveQueue,
			int threadSize, ProxyFactory factory) {
		List<AppConfig> configs = PropertiesUtil.allAppConfigs();
		ExecutorService service = Executors.newFixedThreadPool(15);
		for (int i = 0; i < threadSize; i++) {
			final int threadid = i;
			service.submit(() -> {
				int threadCommitCount = 0;
				BaiduApi api = buildApi(configs.get(threadid % configs.size()), factory);
				while (true) {
					try {
						TextEntity entity = queue.take();
						if ((++threadCommitCount) % 50 == 0) {
							System.out.println(String.format("thread %s commit %s", threadid, threadCommitCount));
						}
						if (executeCondition(entity)) {
							String json = doAction(entity, api);
							if (json == null || json.length() == 0) {
								continue;
							}
							entity.setResponse(json);
							saveQueue.put(entity);
							Thread.sleep(10 + threadid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return service;
	}

	/**
	 * 加载完成的数据
	 * 
	 * @param complete
	 * @return
	 * @throws Exception
	 */
	public Set<Integer> loadCompletes(String complete) throws Exception {
		Set<Integer> ids = new HashSet<Integer>();
		if (new File(complete).exists()) {
			try (TxtFileReader compeletIdRreader = new TxtFileReader(complete)) {
				while (compeletIdRreader.hasNext()) {
					ids.add(Integer.valueOf(compeletIdRreader.readLine()));
				}
			}
		}
		return ids;
	}

	public abstract String doAction(TextEntity entity, BaiduApi api);

	public abstract BaiduApi buildApi(AppConfig config, ProxyFactory factory);

	/**
	 * 判断是否符合执行的条件,如在句法分析时，句子长度不能超出256
	 * 
	 * @return
	 */
	public boolean executeCondition(TextEntity entity) {
		return entity.text.length() * 2 <= 256;
	}

	public void close(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
