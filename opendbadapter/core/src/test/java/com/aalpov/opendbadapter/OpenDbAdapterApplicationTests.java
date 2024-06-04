package com.aalpov.opendbadapter;

import com.aalpov.opendbadapter.service.ClickhouseDatabaseContext;
import com.aalpov.opendbadapter.service.DatabaseContext;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class OpenDbAdapterApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testBatchInsertQueryBuilder() {
		DatabaseContext context = new ClickhouseDatabaseContext();
		DatabaseContext spyContext = spy(context);
		List<Row> rows = new ArrayList<>();
		ClickhouseTable table = mock(ClickhouseTable.class);
		when(table.getName()).thenReturn("INCOMING_CALL");
		Map<String, Object> obj1 = new HashMap<>();
		obj1.put("callDuration", 10);
		obj1.put("callCategory", "spam");
		obj1.put("offnet", false);

		Map<String, Object> obj2 = new HashMap<>();
		obj2.put("callDuration", 20);
		obj2.put("callCategory", "white");
		obj2.put("offnet", true);

		rows.add(new Row(obj1));
		rows.add(new Row(obj2));

		doNothing().when(spyContext).executeQuery(anyString());

		spyContext.batchInsert(rows, table);

		verify(spyContext).executeQuery("INSERT INTO INCOMING_CALL (callDuration,offnet,callCategory) VALUES (10,false,'spam'),(20,true,'white')");
	}
}
