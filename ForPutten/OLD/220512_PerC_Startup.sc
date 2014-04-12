/************************************	*/
/* Percussion Piece		       	*/
/*	STARTUP						*/
/************************************	*/

(
~samplerate = nil;
~numchans = nil;
~numfxchans = nil;
~fx_bus_offset = nil;
~mOut = nil;
~startup = nil;
~midi_setup = nil;
~begin_playback = nil;
~sequencer_stream = nil;

fork{
	~startup = fork{
		~samplerate = 96000;
		~numchans = 2;
		~numfxchans = 8;
		~fx_bus_offset = 0;
		
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
	~midi_setup = fork{
		MIDIClient.init;
		~mOut = Array.fill(8, { |i| MIDIOut(i) });
		TempoClock.default.tempo = 0.8;
	};
	
	////////////////////////////////////////////////////
	
	0.05.wait; 
	
	~begin_playback = fork{
			var p = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/PerC";
			s.waitForBoot(
				postln("/****************************************************/");
				postln("/* 'Per'                                            */");
				postln("/* Michael Murphy 2012                              */");
				postln("/****************************************************/");
				postln(""); postln("");
				postln("STARTUP...");
				postln("________________");
				postln("Loading Synths... ");
				thisProcess.interpreter.executeFile(p ++ "/220512_PerC_Synths.sc");
				1.wait;
				postln("Audio Routing... ");
				thisProcess.interpreter.executeFile(p ++ "/220512_PerC_Main.sc");
				1.wait;
				postln("Loading Sequencer...");
				thisProcess.interpreter.executeFile(p ++ "/220512_PerC_Sequencer.sc");
				1.wait;
				postln("Play!");
			);
			~sequencer_stream = ~sequencer.play;
	};
};
)
//s.queryAllNodes;