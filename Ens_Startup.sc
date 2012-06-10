/************************************	*/
/* Ensemble Piece			       	*/
/*	STARTUP						*/
/************************************	*/

(
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/Ensemble_Piece";
~numchans = nil;
~numfxchans = nil;
~osc_out = nil;
~startup = nil;
~midi_setup = nil;
~begin_playback = nil;
~sequencer_stream = nil;

fork{
	~startup = fork{
		~numchans = 2;
		~numfxchans = 16;
		
		s = Server.local;
		o = Server.local.options;
		//o.inDevice = "Soundflower (16ch)";
		//o.outDevice = "Soundflower (16ch)";
		o.inDevice = "JackRouter";
		o.outDevice = "JackRouter";
		o.blockSize = 512;
		o.hardwareBufferSize = 512;
		o.numOutputBusChannels = 32;
		o.numInputBusChannels = 32;
		o.memSize;
		if(o.memSize<1048576,{o.memSize = 1048576}); // 1GB 2**20
		s.boot;
	};
	
	0.05.wait;
		
	////////////////////////////////////////////////////
		
	~begin_playback = fork{
			s.waitForBoot(
				postln("/****************************************************/");
				postln("/* Ensemble                                         */");
				postln("/* Michael Murphy 2012                              */");
				postln("/****************************************************/");
				postln(""); postln("");
				postln("STARTUP...");
				postln("________________");		
				postln("Setting up OSC... ");
				~osc_setup = fork{
					thisProcess.interpreter.executeFile(~path ++ "/ctosc.sc"); 					~osc_destination = NetAddr("127.0.0.1", 7050);
				};
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
				0.5.wait;
				postln("Play!");
			);
			~sequencer_stream = ~sequencer.play;
	};
};
);
s.queryAllNodes;