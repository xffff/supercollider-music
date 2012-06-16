/************************************	*/
/* Ensemble Piece		             	*/
/*	AUDIO						*/
/************************************	*/

(
~cleanup = nil;
~start_audio = nil;
~input = nil; 
~fx = nil; 
~output = nil; 
~master_fx_bus = nil; 
~buf_a = nil;
~dryaudio = nil;

fork{
	~cleanup = fork{
		postln("Cleaning up");
		s.newAllocators;
		~input.free; ~fx.free; ~output.free;
		~master_fx_bus.free;
		~tamtam_buf.free;
		~dryaudio.free;
		s.sync; 
		postln("Done cleaning up");
	};
	
	0.05.wait; // make sure we're cleaned up before we do the rest
	
	~start_audio = fork{
		(
		// groups
		~input = Group.new(s,\addToHead);   // from sampler
		~fx = Group.new(s, \addToTail);     // fx chain
		~output = Group.new(s, \addToTail); // output
		postln("Groups Allocated");
		
		// busses
		~master_fx_bus  = Bus.audio(s,~numfxchans); // fx bus
		postln("Busses Set");
		
		// buffers
//		~freeze_bufa = Buffer.alloc(s, s.sampleRate * 2, 1); // use these two bufs and xfade
//		~freeze_bufb = Buffer.alloc(s, s.sampleRate * 2, 1); // recbuf synth / freeze synth
		~tamtam_buf = Buffer.alloc(s, s.sampleRate * 16, 1); 
		postln("Buffers Allocated");
		);
		
		////////////////////////////////////////////////////
		
		// persistent synthdefs
		(
		~dryaudio = Synth(\dryaudio, 
			[
			\in, #[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16], 
			\amp, 1.0, 
			\out, ~master_fx_bus,
			], target: ~input, addAction: \addToHead);
			
			postln("Dry Audio On");
			s.queryAllNodes;
		);
		
	}; 
};
)