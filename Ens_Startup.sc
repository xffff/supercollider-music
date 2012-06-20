/************************************	*/
/* Ensemble Piece			       	*/
/*	STARTUP						*/
/************************************	*/

(
~numchans = nil;
~numfxchans = nil;
~osc_out = nil;
~startup = nil;
~midi_setup = nil;
~initialise = nil;
~contimbre_initialise = nil;
~sequencer_stream = nil;


	~startup = {
		~numchans = 2;
		~numfxchans = 16;
		
		s = Server.local;
		o = Server.local.options;
		o.inDevice = "JackRouter";
		o.outDevice = "JackRouter";
		o.blockSize = 1024;
		o.hardwareBufferSize = 1024;
		o.numOutputBusChannels = 32;
		o.numInputBusChannels = 32;
		o.memSize;
		if(o.memSize<1048576,{o.memSize = 1048576}); // 1GB 2**20
		s.boot;
	};
//	~startup.fork;
		
	////////////////////////////////////////////////////
	~contimbre_initialise = {
		postln("Starting conTimbre... (20s)");
		("open "++"\""++~contimbre_path++"/#ePlayer.maxpat"++"\"").unixCmd;
		20.wait;
		
		postln("Loading orchestra... (20s)");
		("open "++"\""++~path++"/Ens_load_orchestra.maxpat"++"\"").unixCmd;
		20.wait;
		
		postln("Setting up OSC... ");
		~osc_setup = fork{
			("open "++"\""++~path++"/OSCFix.maxpat"++"\"").unixCmd;
			thisProcess.interpreter.executeFile(~path ++ "/ctosc.sc"); 					~osc_destination = NetAddr("127.0.0.1", 7050);
		};
	};
//	~contimbre_initialise.fork;
	
	////////////////////////////////////////////////////
	~initialise = {
		s.waitForBoot(
			postln("/****************************************************/");
			postln("/* Ensemble                                         */");
			postln("/* Michael Murphy 2012                              */");
			postln("/****************************************************/");
			postln(""); postln("");
			postln("STARTUP...");
			postln("________________");	
			postln("Loading Synths... ");
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Synths.sc");
			0.5.wait;
			postln("Routing Audio... ");
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Audio.sc");
			0.5.wait;
			postln("Loading Patterns... ");
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Patterns.sc");
			0.5.wait;
			postln("Loading Sequencer...");
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Sequencer.sc");
		);
		//~sequencer_stream = ~sequencer.play;
	};
//	~initialise.fork;
);
s.queryAllNodes;