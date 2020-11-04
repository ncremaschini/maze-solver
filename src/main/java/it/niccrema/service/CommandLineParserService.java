package it.niccrema.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import it.niccrema.App;

public class CommandLineParserService {

    private static final Option OPT_MAP_FILE = Option.builder("m")
                                                .longOpt("map")
                                                .hasArg()
                                                .required()
                                                .argName("mapFileName")
                                                .desc("Map json file name. Files are searched in map folder")
                                                .type(String.class)
                                                .build();

    private static final Option OPT_START_ROOM = Option.builder("r")
                                                .longOpt("room")
                                                .hasArg()
                                                .required()
                                                .argName("startingRoomId")
                                                .desc("Starting room id")
                                                .type(Number.class)
                                                .build();

    private static final Option OPT_ITEMS = Option.builder("i")
                                            .longOpt("items")
                                            .hasArgs()
                                            .required()
                                            .valueSeparator(' ')
                                            .argName("itemsList")
                                            .desc("List of items to collect")
                                            .numberOfArgs(Option.UNLIMITED_VALUES)
                                            .build();

    private final String mapFileName;
    private final Integer startingRoomId;
    private final String[] itemsToCollect;

    private CommandLineParserService(Options options, String[] args) throws ParseException {
    
        CommandLine cli = new DefaultParser().parse(options, args);
        mapFileName = (String) cli.getParsedOptionValue(OPT_MAP_FILE.getOpt());
        startingRoomId = ((Number) cli.getParsedOptionValue(OPT_START_ROOM.getOpt())).intValue();
        itemsToCollect = cli.getOptionValues(OPT_ITEMS.getOpt());
    }

    public static Optional<CommandLineParserService> parse(String[] args) {
        Options options = new Options();
        Stream.of(OPT_MAP_FILE, OPT_START_ROOM, OPT_ITEMS).forEach(options::addOption);
        CommandLineParserService commandLineParserService = null;
        try {
            commandLineParserService = new CommandLineParserService(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(App.class.getName(), options, true);
        }

        return Optional.ofNullable(commandLineParserService);
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public Integer getStartingRoomId() {
        return startingRoomId;
    }

    public String[] getItemsToCollect() {
        return itemsToCollect;
    }
}
