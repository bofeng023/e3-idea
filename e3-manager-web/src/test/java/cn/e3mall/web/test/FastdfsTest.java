package cn.e3mall.web.test;

import cn.e3mall.common.utils.FastDFSClient;
import org.junit.Test;

public class FastdfsTest {
	@Test
	public void testFastdfs() throws Exception{
		//FastDFS���Դ����������ļ�ֻ��д���Ե�ַ
		FastDFSClient client = new FastDFSClient(
				"E:/eclipse/workspaces/e3mall/e3-manager-web/src/main/resources/conf/client.properties");
		String string = client.uploadFile("F:/H/֤��ͼƬ/�ֿ�1.jpg");
		System.out.println(string);
	}
}
