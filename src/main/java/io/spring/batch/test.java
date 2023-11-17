@Configuration
@RequiredArgsConstructor
@Log4j2
public class BatchItemReaderImpl implements IBatchItemReader {
@Override
@Bean
@StepScope
public FlatFileltemReader<SstPoe> getFlatFileltemReader(
@Value("#{stepExecutionContext['fromLine']}") Integer fromLine,
@Value("#{stepExecutionContext['toLine']}") Integer toline,
@Value("#{stepExecutionContext['numberOfRecords']}") Integer totalLines) {
	FlatFileltemReader<SstPoe> itemReader = new FlatFileltemReader<>();
try {
		itemReader.setResource(new FileSystemResource(inputFilePath));
		itemReader.setName("csvFile");
		itemReader.setLineMapper(lineMapper());
		if (fromLine == 1) {
			itemReader.setLinesToSkip (fromLine);
		} else if (toLine.equals(totalLines)) {
			itemReader.setMaxItemCount(toLine - fromLine);
			itemReader.setLinesToSkip(fromLine - 1);
			itemReader.setMaxItemCount(toLine - fromLine);
		} else {
			itemReader.setLinesToSkip(fromLine - 1);
			itemReader.setMaxItemCount(toLine - fromLine + 1);
		} catch (NullPointerException ex) {
			log.error("Input File Exception occured, ", ex);
			log.debug("Flat File Reader Created");
return itemReader;
}
		
private Range[] getRanges() {
Range[] ranges = new Range[fieldRanges.size());
int iterator = 0;
for (Map Entry<integer, Integer> iter: fieldRanges.entrySet()) {
ranges[iterator++] = new Rangeliter.getKey(), iter.getValue()); 
}
return ranges;
}


private LineMapper<5stPoe> lineMapper() {
		DefaultLineMapper<SstPoe> lineMapper = new DefaultLineMapper<>();
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(getRanges()); tokenizer.setNames(fieldNames);
		FieldSetMapper<SstPoe> fieldSetMapper field->{
		SstPoe sstPoe = new SstPoe();
		sstPoe.setBankidNo(field.readString(0).trim());
		sstPoe.setHostPlatform No(field.readInt(1));
		sstPoe.setPmtConMicrNo(field.readInt(2));
		sstPoe.setGeDestNo(field.readInt(3));
		sstPoe.setHostNo(field.readShort(4));
		sstPoe.setBusLineTpNo(field.readInt(5)); 
		String ProdBrndNoStr-field.readString(6);
		sstPoe.setProdBrndNo(convertStringToShort (ProdBrndNoStr));
		sstPoe.setissuerNo(field.readShort(7));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date format.parse(field.readString(8));
			Timestamp effts= new Timestamp[date.getTime());
			sstPoe.setLastUpdateTs(effts);
		}catch (ParseException exception) {
			log.error("error occured in date format: ",exception);
}
return sstPoe;
		}:
lineMapper.setLineTokenizer(tokenizer);
lineMapper.setFieldSetMapper(fieldSetMapper);

return lineMapper;

}

private Short convertStringToShort(String prodbrndno) {
		switch(prodbrndno) {
		case "A":
		return 3;
		case "V": return 4;
		
		case "M":
		
		return 5;
		
		case "S":
		
		return 8;
		
		case "G": return 9;
		
		default: return 0;
		
		}
}

}