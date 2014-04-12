/************************************	*/
/* Untitled Piece    			       	*/
/*	STARTUP			         			*/
/************************************	*/

(
~osc_out = nil;
~startup = nil;
~midi_setup = nil;
~initialise = nil;
~contimbre_initialise = nil;
~sequencer_stream = nil;

~startup = {
	s = Server.local;
	o = Server.local.options;
	o.inDevice = "JackRouter";
	o.outDevice = "JackRouter";
	o.blockSize = ~buffersize;
	o.hardwareBufferSize = ~buffersize;
	o.numOutputBusChannels = ~numchans;
	o.numInputBusChannels = ~numchans;
	if(o.memSize<1048576,{o.memSize = 1048576}); // 1GB 2**20
	s.boot;

	thisProcess.interpreter.executeFile(~path ++ "/ctevent.scd");
	~osc_destination = NetAddr("127.0.0.1", 3000);
};

////////////////////////////////////////////////////
~contimbre_initialise = {
	postln("Starting conTimbre... ");
	("open "++"\""++~contimbre_path++"ePlayer_maxmsp/#conTimbre ePlayer.maxpat"++"\"").unixCmd;
	5.wait;

//	postln("Loading orchestra... ");
//	("open "++"\""++~path++"/load_orchestra.maxpat"++"\"").unixCmd;
//	5.wait;
};

////////////////////////////////////////////////////
~initialise = {
	s.waitForBoot({
		s.freeAll;
		0.5.wait;

		postln("STARTUP...");
		postln("________________");
		postln("Loading Synths... ");
		thisProcess.interpreter.executeFile(~path ++ "/synths.scd");
		0.5.wait;
		postln("Routing Audio... ");
		thisProcess.interpreter.executeFile(~path ++ "/audio.scd");
		0.5.wait;
		postln("Loading Mixer... ");
		thisProcess.interpreter.executeFile(~path ++ "/mixer.scd");
		0.5.wait;
		postln("Loading Patterns... ");
		thisProcess.interpreter.executeFile(~path ++ "/patterns.scd");
		0.5.wait;
		postln("Loading Sequencer...");
		thisProcess.interpreter.executeFile(~path ++ "/sequencer.scd");
	});
};
);