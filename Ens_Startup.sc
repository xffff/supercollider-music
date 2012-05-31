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
		~numfxchans = 8;
		
		s = Server.local;
		o = Server.local.options;
		o.inDevice = "Soundflower (16ch)";
		o.outDevice = "Soundflower (16ch)";
		o.blockSize = 512;
		o.hardwareBufferSize = 512;
		o.numOutputBusChannels = 16;
		o.numInputBusChannels = 16;
		o.memSize;
		if(o.memSize<1048576,{o.memSize = 1048576}); // 1GB 2**20
		s.boot;
	};
	
	0.05.wait;
	
	////////////////////////////////////////////////////
	~osc_setup = fork{
		thisProcess.interpreter.executeFile(~path ++ "/ctosc.sc"); // conTimbre osc event type
		~osc_destination = NetAddr("127.0.0.1", 7050);
	};
	
	////////////////////////////////////////////////////
	
	0.05.wait; 
	
	~begin_playback = fork{
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
				1.wait;
				postln("Routing Audio... ");
				thisProcess.interpreter.executeFile(~path ++ "/Ens_Audio.sc");
				1.wait;
				postln("Loading Patterns... ");
				thisProcess.interpreter.executeFile(~path ++ "/Ens_Patterns.sc");
				1.wait;
				postln("Loading Sequencer...");
				thisProcess.interpreter.executeFile(~path ++ "/Ens_Sequencer.sc");
				1.wait;
				postln("Play!");
			);
			~sequencer_stream = ~sequencer.play;
	};
};
)
//s.queryAllNodes;